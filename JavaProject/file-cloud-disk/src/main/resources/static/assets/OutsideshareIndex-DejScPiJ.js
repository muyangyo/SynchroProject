import{_ as v,r as P,l as $,h as y,q as M,o as O,d as w,w as c,a as l,b as d,I as k,t as D,e as L,u as N,J as U,f as q,E,i as u,K as X}from"./index-jlGd6iTg.js";import{I as z,s as A}from"./FileSizeConverter-JwxVJbPW.js";import{e as T,R as x}from"./RequestTool-D0jUfVk2.js";const B={class:"cloud-index-wrapper"},V={class:"cloud-index-content"},G=["onClick"],K={class:"file-name"},j={__name:"OutsideshareIndex",setup(W){const r=U(),b=q(),f=r.query.shareCode,t=Object.freeze({IMAGE:"IMAGE",VIDEO:"VIDEO",AUDIO:"AUDIO",TEXT:"TEXT",PDF:"PDF",DOCX:"DOCX",FOLDER:"FOLDER",UNKNOWN:"UNKNOWN",COMPRESSED:"COMPRESSED",EXCEL:"EXCEL",PPT:"PPT",CODE:"CODE"}),p=P([]);P({fileName:"",fileSize:"",modifiedTime:"",filePath:"",mountRootPath:"",fileType:{category:t.UNKNOWN,typeName:"",mimeType:""}});const g=o=>{o.data.forEach(e=>{e.fileType.category===t.FOLDER&&(e.fileSize="")}),p.value=o.data};$(()=>{if(!f){y.error("分享链接无效");return}C(r.query.parentPath)});const C=o=>{T(x.GET,`/file/getShareFile?shareCode=${f}&parentPath=${o||""}`,"",!1,!0).then(e=>{e.statusCode==="SUCCESS"&&e.data?(g(e),p.value=e.data):y.error(e.errMsg)})},S=o=>({[t.VIDEO]:"#icon-shipin",[t.IMAGE]:"#icon-tupian",[t.AUDIO]:"#icon-yinpin",[t.TEXT]:"#icon-TXT",[t.PDF]:"#icon-PDF",[t.DOCX]:"#icon-word",[t.FOLDER]:"#icon-wenjianjia",[t.COMPRESSED]:"#icon-yasuobao",[t.EXCEL]:"#icon-excel",[t.PPT]:"#icon-PPT",[t.CODE]:"#icon-jiaoben",[t.UNKNOWN]:"#icon-weizhi"})[o]||"#icon-weizhi",I=(o,e)=>{if(e.fileType.category===t.FOLDER){const n=r.query.parentPath||"";let i=n?`${n}/${e.fileName}`:e.fileName;b.push({query:{shareCode:f,parentPath:i}})}};M(()=>r.query.parentPath,()=>{const o=r.query.parentPath||"";C(o)});const F=(o,e)=>{let n=null;e.fileType.category===t.FOLDER&&(n=E.alert("正在生成压缩包，请耐心等待!","提示",{confirmButtonText:"确定",type:"warning",showClose:!1,closeOnClickModal:!1,closeOnPressEscape:!1,showCancelButton:!1,showConfirmButton:!1}));const i=r.query.parentPath||"";T(x.GET,`/file/preparingDownloadShareFile?shareCode=${f}&fileName=${e.fileName}&parentPath=${i}`,"",!1,!1,6e4).then(s=>{if(n&&E.close(),s.statusCode==="SUCCESS"&&s.data){const h=s.data.url,m=s.data.fileName;let _=`${location.origin}/api${h}`;const a=document.createElement("a");a.href=_,a.setAttribute("download",m),document.body.appendChild(a),a.click(),document.body.removeChild(a)}}).catch(s=>{n&&E.close(),console.error("文件下载失败",s)})};return(o,e)=>{const n=u("el-col"),i=u("el-table-column"),s=u("el-button"),h=u("el-tooltip"),m=u("el-table"),_=u("el-row");return O(),w(_,{gutter:20,class:"cloud-index-container"},{default:c(()=>[l(n,{span:3}),l(n,{span:18},{default:c(()=>[d("div",B,[e[0]||(e[0]=d("div",{class:"cloud-index-header"},[d("h2",{class:"cloud-index-title"},"分享文件")],-1)),d("div",V,[l(m,{data:p.value,"default-sort":{prop:"fileName",order:"descending"},class:"full-width-table custom-table",height:"600px"},{default:c(()=>[l(i,{prop:"fileName",label:"文件名",sortable:"","min-width":"100px","show-overflow-tooltip":!0},{default:c(a=>[d("div",{class:"cell-content",onClick:R=>I(a.$index,a.row)},[a.row.fileType.category?(O(),w(z,{key:0,name:S(a.row.fileType.category)},null,8,["name"])):k("",!0),d("span",K,D(a.row.fileName),1)],8,G)]),_:1}),l(i,{prop:"fileSize",label:"大小",sortable:""},{default:c(a=>[L(D(N(A)(a.row.fileSize)),1)]),_:1}),l(i,{prop:"operation",label:"操作","min-width":"100px"},{default:c(a=>[l(h,{content:"下载",placement:"top","show-arrow":!1},{default:c(()=>[l(s,{type:"success",size:"small",icon:N(X),onClick:R=>F(a.$index,a.row)},null,8,["icon","onClick"])]),_:2},1024)]),_:1})]),_:1},8,["data"])])])]),_:1}),l(n,{span:3})]),_:1})}}},Q=v(j,[["__scopeId","data-v-842c17ab"]]);export{Q as default};