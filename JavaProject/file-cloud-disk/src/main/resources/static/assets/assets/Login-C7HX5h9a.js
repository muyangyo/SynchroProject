import{_ as I,u as K,r as _,a as O,o as R,e as f,g as b,i as k,E as m,c as N,b as v,d as a,w as o,R as y,f as p,h as U,j as w,U as C,k as D,l as M}from"./index-DygJYWF7.js";const P={class:"login-container"},T={class:"login-box"},q={__name:"Login",setup(L){const S=K(),c=_(null),u=O(),s=_({username:"",password:"",secretKey:""}),V={username:[{required:!0,type:"string",message:"请输入账号",trigger:"blur"}],password:[{required:!0,type:"string",message:"请输入密码",trigger:"blur"}],secretKey:[{required:!1,type:"string"}]},E=()=>{c.value.validate(async d=>{var e,l;if(d){let r=await f(y.GET,"/admin/getPublicKey","");u.setPublicKey(r.data);const i={username:u.encryptData(s.value.username),password:u.encryptData(s.value.password),key:s.value.secretKey?u.encryptData(s.value.secretKey):""};try{const t=await f(y.POST,"/admin/login",i);t.data===!0&&t.statusCode==="SUCCESS"?(C.login(D.admin,s.value.username,""),m({message:`欢迎回来, ${s.value.username}`,type:"success"}),await S.push(M.managerRouterBaseUrl)):m.error(t.errMsg||"账号或密码错误")}catch(t){m.error(((l=(e=t.response)==null?void 0:e.data)==null?void 0:l.message)||"登录失败")}}})},x=()=>{c.value.resetFields()},g=_(!1);return R(()=>{f(y.GET,"/admin/remoteOperationIsOpen","").then(d=>{d.data===!1?(localStorage.setItem(b.ADMIN_OPERATION_IS_OPEN,"false"),k()||(g.value=!0,m.error({message:"管理操作只能在 <b>127.0.0.1</b> 访问!<br>如果需要远程操作,请设置允许远程访问!",dangerouslyUseHTMLString:!0,duration:5e3,center:!0}))):localStorage.setItem(b.ADMIN_OPERATION_IS_OPEN,"true")})}),(d,e)=>{const l=p("el-input"),r=p("el-form-item"),i=p("el-button"),t=p("el-form");return U(),N("div",P,[v("div",T,[e[5]||(e[5]=v("div",{style:{"text-align":"center","margin-bottom":"20px","font-size":"36px","font-weight":"bold"}}," Login ",-1)),a(t,{model:s.value,rules:V,ref_key:"loginForm",ref:c,"label-width":"80px"},{default:o(()=>[a(r,{label:"账号",prop:"username"},{default:o(()=>[a(l,{modelValue:s.value.username,"onUpdate:modelValue":e[0]||(e[0]=n=>s.value.username=n),placeholder:"请输入账号"},null,8,["modelValue"])]),_:1}),a(r,{label:"密码",prop:"password"},{default:o(()=>[a(l,{type:"password",modelValue:s.value.password,"onUpdate:modelValue":e[1]||(e[1]=n=>s.value.password=n),placeholder:"请输入密码"},null,8,["modelValue"])]),_:1}),a(r,{label:"密钥",prop:"secretKey"},{default:o(()=>[a(l,{modelValue:s.value.secretKey,"onUpdate:modelValue":e[2]||(e[2]=n=>s.value.secretKey=n),placeholder:"请输入密钥（可选）"},null,8,["modelValue"])]),_:1}),a(r,null,{default:o(()=>[a(i,{type:"primary",onClick:E,disabled:g.value},{default:o(()=>e[3]||(e[3]=[w("登录")])),_:1},8,["disabled"]),a(i,{onClick:x,disabled:g.value},{default:o(()=>e[4]||(e[4]=[w("重置")])),_:1},8,["disabled"])]),_:1})]),_:1},8,["model"])])])}}},A=I(q,[["__scopeId","data-v-7d781a56"]]);export{A as default};
