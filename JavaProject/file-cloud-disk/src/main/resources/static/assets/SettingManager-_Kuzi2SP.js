import{e as g,R as n,o as j}from"./RequestTool-BdeTmx98.js";import{i as P}from"./IsLocalHost-D6T0nX5p.js";import{r as E}from"./RSAEncryptUtils-Ybv_6rCu.js";import{_ as W,r as f,l as J,h as o,q as Q,o as X,c as Y,b as p,a as r,w as l,e as d,u as s,t as q,E as x,i as Z,s as c,v as S,x as $,y as ee,z as T,A as L,B as ae,C as U,D as te}from"./index-BktgtzP8.js";const re={class:"content-area"},se={class:"table-header"},le={class:"table-container"},oe=["onClick"],ue={class:"table-header"},ie={class:"table-container"},de={class:"dialog-footer"},ne={__name:"SettingManager",setup(me){const _=f([]);J(()=>{b(),M()});const w=f("password"),k=()=>{w.value=w.value==="password"?"text":"password"},b=()=>{g(n.GET,"/shareFolderManager/getShareFolderList","",!1,!1).then(a=>{a.data&&a.statusCode==="SUCCESS"?_.value=a.data:o.error(a.errMsg?a.errMsg:"获取共享目录列表失败")})},O=()=>{P()?j({method:n.POST,url:"/shareFolderManager/addShareFolder",timeout:0}).then(a=>{a.data===!0&&a.statusCode==="SUCCESS"?(o.success("添加成功"),b()):o.error(a.errMsg?a.errMsg:"添加失败")}):x.prompt("请输入服务器上的绝对路径地址","添加目录",{confirmButtonText:"确定",cancelButtonText:"取消",inputPattern:/^(\/|\/?(?:[^\/\*?"<>|]+\/)*[^\/\*?"<>|]*)$/,inputErrorMessage:"路径格式不正确"}).then(({value:a})=>{a!==null&&a.length>0?(a=a.trim(),g(n.POST,"/shareFolderManager/addShareFolder",{path:a},!1,!0).then(e=>{e.data===!0&&e.statusCode==="SUCCESS"?(o.success("添加成功"),b()):o.error(e.errMsg?e.errMsg:"添加失败")})):o.error("路径不能为空!")}).catch()},G=a=>{P()?g(n.POST,"/shareFolderManager/openFolder",{path:a},!1,!1).then(e=>{e.data===!0&&e.statusCode==="SUCCESS"?o.success("打开成功"):o.error(e.errMsg?e.errMsg:"调用系统资源文件管理器失败!")}):o.error("请在本地环境下操作!")},R=a=>{x.confirm("确定删除该目录: <br>"+a,"提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning",dangerouslyUseHTMLString:!0}).then(()=>{g(n.POST,"/shareFolderManager/deleteShareFolder",{path:a},!1,!1).then(e=>{e.data===!0&&e.statusCode==="SUCCESS"?(o.success("删除成功"),b()):o.error(e.errMsg?e.errMsg:"删除失败")})})},v=f(!1),C=f(!1),u=f({}),V=f([]),F=f(null),M=()=>{g(n.GET,"/userManager/getUserList","",!1,!1).then(a=>{a.data&&a.statusCode==="SUCCESS"?V.value=a.data:o.error(a.errMsg?a.errMsg:"获取用户列表失败")})},A={r:"读",w:"写",d:"删除"},B={r:1,w:2,d:3},I=(a,e)=>{const i=a.permissions.reduce((m,y)=>m+B[y],0);return e.permissions.reduce((m,y)=>m+B[y],0)-i},h=f({username:[{required:!0,message:"请输入用户名",trigger:"blur"},{min:1,max:30,message:"长度在 1 到 30 个字符",trigger:"blur"}],password:[{required:!1,message:"请输入密码",trigger:"blur"},{min:1,max:32,message:"长度在 1 到 32 个字符",trigger:"blur"}],permissions:[{type:"array"}]}),N=()=>{C.value=!1,h.value.password=[{required:!0,message:"请输入密码",trigger:"blur"},{min:1,max:32,message:"长度在 1 到 32 个字符",trigger:"blur"}],u.value={username:"",password:"",permissions:[]},v.value=!0},z=a=>{C.value=!0,h.value.password=[{required:!1,message:"请输入密码",trigger:"blur"},{min:1,max:32,message:"长度在 1 到 32 个字符",trigger:"blur"}],u.value={...a,password:""},v.value=!0},H=()=>{F.value.validate(a=>{if(a)g(n.GET,"/userManager/getPublicKey","").then(e=>{E.setPublicKey(e.data);const i={username:E.encryptData(u.value.username),permissions:u.value.permissions};u.value.password&&(i.password=E.encryptData(u.value.password)),C.value?g(n.POST,"/userManager/updateUser",i,!1,!0).then(t=>{t.data===!0&&t.statusCode==="SUCCESS"?(o.success("修改成功"),v.value=!1,M()):o.error(t.errMsg?t.errMsg:"修改失败")}):g(n.POST,"/userManager/addUser",i,!1,!0).then(t=>{t.data===!0&&t.statusCode==="SUCCESS"?(o.success("添加成功"),v.value=!1,M()):o.error(t.errMsg?t.errMsg:"添加失败")})});else return o.warning("请填写必填项并输入合法数据!"),!1})},K=(a,e)=>{x.confirm("确定删除该用户: "+e.username,"提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{g(n.DELETE,`/userManager/deleteUser?username=${e.username}`,"",!1,!1).then(i=>{i.data===!0&&i.statusCode==="SUCCESS"?(o.success("删除成功"),M()):o.error(i.errMsg?i.errMsg:"删除失败")})})},D=f("");return Q(C,a=>{D.value=a?"修改用户":"添加用户"},{immediate:!0}),(a,e)=>{const i=Z("el-icon");return X(),Y("div",re,[p("div",se,[e[6]||(e[6]=p("h2",null,"共享目录列表",-1)),r(s(c),{type:"primary",onClick:O},{default:l(()=>e[5]||(e[5]=[d("添加目录")])),_:1})]),p("div",le,[r(s($),{data:_.value,style:{width:"100%"},"max-height":"200px"},{default:l(()=>[r(s(S),{label:"路径",prop:"path","min-width":"180px","show-overflow-tooltip":!0,sortable:""},{default:l(t=>[p("span",{onClick:m=>G(t.row),style:{cursor:"pointer"}},q(t.row),9,oe)]),_:1}),r(s(S),{label:"操作"},{default:l(t=>[r(s(c),{type:"danger",onClick:m=>R(t.row),size:"small"},{default:l(()=>e[7]||(e[7]=[d("删除")])),_:2},1032,["onClick"])]),_:1})]),_:1},8,["data"])]),p("div",ue,[e[9]||(e[9]=p("h2",null,"用户管理",-1)),r(s(c),{type:"primary",onClick:N},{default:l(()=>e[8]||(e[8]=[d("添加用户")])),_:1})]),p("div",ie,[r(s($),{data:V.value,style:{width:"100%"},"max-height":"200px"},{default:l(()=>[r(s(S),{label:"用户",prop:"username",sortable:""}),r(s(S),{label:"访问权限",sortable:"","sort-method":I},{default:l(t=>[d(q(t.row.permissions.map(m=>A[m]).join(", ")),1)]),_:1}),r(s(S),{label:"操作"},{default:l(t=>[r(s(c),{type:"warning",onClick:m=>z(t.row)},{default:l(()=>e[10]||(e[10]=[d("修改")])),_:2},1032,["onClick"]),r(s(c),{type:"danger",onClick:m=>K(t.$index,t.row)},{default:l(()=>e[11]||(e[11]=[d("删除")])),_:2},1032,["onClick"])]),_:1})]),_:1},8,["data"])]),r(s(te),{title:D.value,modelValue:v.value,"onUpdate:modelValue":e[4]||(e[4]=t=>v.value=t),width:"30%",draggable:!0},{footer:l(()=>[p("span",de,[r(s(c),{onClick:e[3]||(e[3]=t=>v.value=!1)},{default:l(()=>e[15]||(e[15]=[d("取消")])),_:1}),r(s(c),{type:"primary",onClick:H},{default:l(()=>e[16]||(e[16]=[d("确定")])),_:1})])]),default:l(()=>[r(s(ee),{model:u.value,rules:h.value,ref_key:"userForm",ref:F,"label-width":"120px"},{default:l(()=>[r(s(T),{label:"用户",prop:"username"},{default:l(()=>[r(s(L),{modelValue:u.value.username,"onUpdate:modelValue":e[0]||(e[0]=t=>u.value.username=t),disabled:C.value},null,8,["modelValue","disabled"])]),_:1}),r(s(T),{label:C.value?"新密码":"密码",prop:"password"},{default:l(()=>[r(s(L),{type:w.value,modelValue:u.value.password,"onUpdate:modelValue":e[1]||(e[1]=t=>u.value.password=t),onClick:k,"show-password":""},{suffix:l(()=>[r(i,{icon:w.value==="password"?"View":"Hide",onClick:k,style:{cursor:"pointer"}},null,8,["icon"])]),_:1},8,["type","modelValue"])]),_:1},8,["label"]),r(s(T),{label:"访问权限",prop:"permissions"},{default:l(()=>[r(s(ae),{modelValue:u.value.permissions,"onUpdate:modelValue":e[2]||(e[2]=t=>u.value.permissions=t)},{default:l(()=>[r(s(U),{value:"r"},{default:l(()=>e[12]||(e[12]=[d("读")])),_:1}),r(s(U),{value:"w"},{default:l(()=>e[13]||(e[13]=[d("写")])),_:1}),r(s(U),{value:"d"},{default:l(()=>e[14]||(e[14]=[d("删除")])),_:1})]),_:1},8,["modelValue"])]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["title","modelValue"])])}}},ce=W(ne,[["__scopeId","data-v-63ba0353"]]);export{ce as default};
