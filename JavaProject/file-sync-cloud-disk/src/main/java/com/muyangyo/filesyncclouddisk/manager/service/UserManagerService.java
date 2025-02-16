package com.muyangyo.filesyncclouddisk.manager.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.exception.IllegalLoginWithoutRSA;
import com.muyangyo.filesyncclouddisk.common.model.dto.UserDTO;
import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import com.muyangyo.filesyncclouddisk.common.model.meta.User;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.model.vo.UserListVO;
import com.muyangyo.filesyncclouddisk.common.utils.EasyTimedCache;
import com.muyangyo.filesyncclouddisk.common.utils.MD5Utils;
import com.muyangyo.filesyncclouddisk.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.util.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/17
 * Time: 19:01
 */
@Service
@Slf4j
public class UserManagerService {

    public static final Set<String> ALLOWED_PERMISSIONS = new HashSet<>(Arrays.asList("r", "w", "d"));
    @Resource
    private UserMapper userMapper;

    @Resource
    private OperationLogService operationLogService;

    @Resource
    private Setting setting;

    public Result getUserList(HttpServletRequest request) {
        User user = new User();
        user.setAccountStatus(1);
        List<User> userList = userMapper.selectByDynamicCondition(user);

        ArrayList<UserListVO> result = new ArrayList<>(userList.size());
        for (User u : userList) {
            UserListVO userListVO = new UserListVO();
            userListVO.setUsername(u.getUsername());
            userListVO.setPassword("");

            List<String> permissionList = new ArrayList<>(u.getPermissions().length());
            for (char c : u.getPermissions().toCharArray()) {
                permissionList.add(String.valueOf(c));
            }


            userListVO.setPermissions(permissionList);

            result.add(userListVO);
        }
        operationLogService.addLogFromRequest("管理员操作: 获取用户列表", OperationLevel.INFO, request);
        return Result.success(result);
    }


    /**
     * 创建用户
     *
     * @param username       用户名
     * @param password       密码
     * @param permissionList 权限
     * @param request
     * @return 创建结果
     */
    public Result createUser(String username, String password, ArrayList<String> permissionList, HttpServletRequest request) {
        User user = userMapper.selectByUsername(username); // 判断用户名是否存在

        if (user != null) {
            return Result.fail("用户名已存在");
        }

        //1. 空的话: 没有权限 2. r:读权限 3. w:写权限 4. d:删除权限
        StringBuilder permission = new StringBuilder();
        if (!permissionList.isEmpty()) {
            for (String p : permissionList) {
                if (ALLOWED_PERMISSIONS.contains(p)) {
                    permission.append(p);
                }
            }
        }

        String encipher = MD5Utils.encipher(password);
        user = new User(RandomUtil.randomString(10), username, encipher, new Date(), new Date(), 1, permission.toString());
        userMapper.insertByDynamicCondition(user);
        operationLogService.addLogFromRequest("管理员操作: 管理员创建用户[ " + username + " ]", OperationLevel.WARNING, request);
        return Result.success(true);
    }


    public void decryptLogin(UserDTO userDTO, String ip) {
        EasyTimedCache<String, PrivateKey> rasCache = setting.getRasCache();
        PrivateKey privateKey = rasCache.get(ip); // 根据IP获取私钥
        // 三次解密
        RSA rsa = SecureUtil.rsa();
        rsa.setPrivateKey(privateKey);
        try {
            userDTO.setUsername(rsa.decryptStr(userDTO.getUsername(), KeyType.PrivateKey));
            if (StringUtils.hasLength(userDTO.getPassword())) {
                userDTO.setPassword(rsa.decryptStr(userDTO.getPassword(), KeyType.PrivateKey));
            }
        } catch (Exception e) {
            throw new IllegalLoginWithoutRSA("修改用户信息时解密失败，请检查RSA私钥是否正确！");
        }
        log.info("新的用户信息解密成功!");
    }

    /**
     * 生成公钥和私钥，并缓存私钥
     *
     * @param ip 登录IP
     * @return 公钥
     */
    public Result getPublicKey(String ip) {
        RSA rsa = SecureUtil.rsa();
        EasyTimedCache<String, PrivateKey> rasCache = setting.getRasCache();
        rasCache.put(ip, rsa.getPrivateKey()); // 缓存私钥

        String publicKey = rsa.getPublicKeyBase64(); // 发送公钥给客户端
        return Result.success(publicKey);
    }

    public Result updateUser(String username, String password, ArrayList<String> permissionList, HttpServletRequest request) {
        User user = userMapper.selectByUsername(username); // 判断用户名是否存在
        if (user == null) {
            return Result.fail("用户名不存在");
        }

        //1. 空的话: 没有权限 2. r:读权限 3. w:写权限 4. d:删除权限
        StringBuilder permission = new StringBuilder();
        if (!permissionList.isEmpty()) {

            for (String p : permissionList) {
                if (ALLOWED_PERMISSIONS.contains(p)) {
                    permission.append(p);
                }
            }
        }
        user.setPermissions(permission.toString());


        // 如果有密码，则加密
        if (StringUtils.hasLength(password)) {
            String encipher = MD5Utils.encipher(password);
            user.setPassword(encipher);
        }

        userMapper.updateByUserId(user);
        operationLogService.addLogFromRequest("管理员操作: 管理员更新用户[ " + username + " ]", OperationLevel.WARNING, request);
        return Result.success(true);
    }

    public Result deleteUser(String username, HttpServletRequest request) {
        User user = userMapper.selectByUsername(username); // 判断用户名是否存在
        if (user == null) {
            return Result.fail("用户名不存在");
        }
        user.setAccountStatus(0);
        userMapper.deleteByUserId(user.getUserId());
        operationLogService.addLogFromRequest("管理员操作: 管理员删除用户[ " + username + " ]", OperationLevel.IMPORTANT, request);
        return Result.success(true);
    }
}
