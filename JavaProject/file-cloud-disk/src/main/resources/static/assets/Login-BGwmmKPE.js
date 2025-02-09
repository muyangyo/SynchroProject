import{_ as V,r as c,o as R,c as C,aa as S,b as f,a as s,w as r,e as b,f as k,U as E,R as U,h as p,i as d}from"./index-CIM8G_E7.js";import{e as g,R as _}from"./RequestTool-lXjPAhMY.js";import{r as m}from"./RSAEncryptUtils-Ybv_6rCu.js";const q={class:"login-container"},F={class:"login-box"},L={__name:"Login",setup(N){const w=k(),i=c(null),a=c({username:"",password:""}),y={username:[{required:!0,type:"string",message:"请输入账号",trigger:"blur"}],password:[{required:!0,type:"string",message:"请输入密码",trigger:"blur"}]},x=()=>{i.value.validate(async v=>{var e,l;if(v){let o=await g(_.GET,"/user/getPublicKey","");m.setPublicKey(o.data);const n={username:m.encryptData(a.value.username),password:m.encryptData(a.value.password)};try{const t=await g(_.POST,"/user/login",n);t.statusCode==="SUCCESS"&&t.data===!0?(E.login(U.user,a.value.username),p({message:`欢迎回来, ${a.value.username}`,type:"success"}),await w.push("/user")):p.error(t.errMsg||"账号或密码错误")}catch(t){p.error(((l=(e=t.response)==null?void 0:e.data)==null?void 0:l.message)||"登录失败")}}})},h=()=>{i.value.resetFields()};return(v,e)=>{const l=d("el-input"),o=d("el-form-item"),n=d("el-button"),t=d("el-form");return R(),C("div",q,[e[5]||(e[5]=S('<div class="warning-banner" data-v-aeb51fda><div class="banner-content" data-v-aeb51fda><div class="banner-title" data-v-aeb51fda>⚠️ 提示</div><div class="banner-text" data-v-aeb51fda>由于本服务器带宽有限，部分功能受限制，此处展示非完全体</div><div class="banner-text" data-v-aeb51fda> 测试账号(仅读权限): <span class="highlight-text" data-v-aeb51fda>123</span> 密码: <span class="highlight-text" data-v-aeb51fda>3ora7wfi53</span></div><div class="banner-section" data-v-aeb51fda><div class="banner-subtitle" data-v-aeb51fda>Ps:</div><ul class="banner-list" data-v-aeb51fda><li data-v-aeb51fda>若需要完整体验，请下载 <span class="highlight-text" data-v-aeb51fda>文件云盘</span> 文件夹内的压缩包</li><li data-v-aeb51fda>若需要更多权限，请联系作者QQ: <span class="highlight-text" data-v-aeb51fda>3560775787</span></li></ul></div></div></div>',1)),f("div",F,[e[4]||(e[4]=f("div",{style:{"text-align":"center","margin-bottom":"20px","font-size":"36px","font-weight":"bold"}}," Login ",-1)),s(t,{model:a.value,rules:y,ref_key:"loginForm",ref:i,"label-width":"80px"},{default:r(()=>[s(o,{label:"账号",prop:"username"},{default:r(()=>[s(l,{modelValue:a.value.username,"onUpdate:modelValue":e[0]||(e[0]=u=>a.value.username=u),placeholder:"请输入账号"},null,8,["modelValue"])]),_:1}),s(o,{label:"密码",prop:"password"},{default:r(()=>[s(l,{type:"password",modelValue:a.value.password,"onUpdate:modelValue":e[1]||(e[1]=u=>a.value.password=u),placeholder:"请输入密码"},null,8,["modelValue"])]),_:1}),s(o,null,{default:r(()=>[s(n,{type:"primary",onClick:x},{default:r(()=>e[2]||(e[2]=[b("登录")])),_:1}),s(n,{onClick:h},{default:r(()=>e[3]||(e[3]=[b("重置")])),_:1})]),_:1})]),_:1},8,["model"])])])}}},M=V(L,[["__scopeId","data-v-aeb51fda"]]);export{M as default};
