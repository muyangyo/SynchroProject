import{_ as M,r as u,l as x,n as I,o as y,c as z,a as o,w as p,e as f,t as B,b as h,h as v,E as O,m as R,i,p as D}from"./index-jlGd6iTg.js";import{e as b,R as L}from"./RequestTool-D0jUfVk2.js";const P={class:"operation-log-manager"},k={class:"pagination-container"},A={class:"button-group"},G={__name:"OperationLog",setup(U){const T=({prop:e,order:t})=>{if(e==="operationTime")n.value.sort((a,s)=>{const l=new Date(a.operationTime).getTime(),c=new Date(s.operationTime).getTime();return t==="descending"?c-l:l-c});else if(e==="operationLevel"){const a={IMPORTANT:3,WARNING:2,INFO:1};n.value.sort((s,l)=>t==="ascending"?a[s.operationLevel]-a[l.operationLevel]:a[l.operationLevel]-a[s.operationLevel])}},n=u([]),g=u(0),d=u(12),r=u(1),_=e=>{b(L.GET,`/operationLog/getLogList?page=${e}&pageSize=${d.value}`,"",!1,!1).then(t=>{t.data&&t.statusCode==="SUCCESS"?(n.value=t.data.list,g.value=t.data.total):v.error(t.errMsg?t.errMsg:"获取操作日志失败!")})};x(()=>{r.value=1,_(r.value)}),I(()=>{n.value=[]});const C=e=>{d.value=e,r.value=1,_(r.value)},S=e=>{r.value=e,_(e)},w=()=>{O.confirm("确定要删除所有操作日志吗？","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"error",icon:R(D)}).then(()=>{b(L.DELETE,"/operationLog/deleteLog","",!1,!1).then(e=>{e.data===!0&&e.statusCode==="SUCCESS"?(v.success("操作日志删除成功!"),n.value=[],g.value=0):v.error(e.errMsg?e.errMsg:"删除操作日志失败!")})})},N=e=>{switch(e){case"INFO":return"info";case"WARNING":return"warning";case"IMPORTANT":return"danger";default:return""}};return(e,t)=>{const a=i("el-table-column"),s=i("el-tag"),l=i("el-table"),c=i("el-pagination"),E=i("el-button");return y(),z("div",P,[o(l,{data:n.value,stripe:"","default-sort":{prop:"operationTime",order:"descending"},onSortChange:T},{default:p(()=>[o(a,{prop:"operationTime",label:"操作时间",sortable:"custom"}),o(a,{prop:"operation",label:"操作","show-overflow-tooltip":!0,"min-width":"150"}),o(a,{prop:"userName",label:"用户名",sortable:""}),o(a,{prop:"userIp",label:"用户IP",sortable:""}),o(a,{prop:"operationLevel",label:"操作级别",sortable:"custom"},{default:p(m=>[o(s,{type:N(m.row.operationLevel)},{default:p(()=>[f(B(m.row.operationLevel),1)]),_:2},1032,["type"])]),_:1})]),_:1},8,["data"]),h("div",k,[o(c,{background:"",layout:"total, prev, pager, next, sizes","page-sizes":[8,12,15],"page-size":d.value,total:g.value,"current-page":r.value,onSizeChange:C,onCurrentChange:S},null,8,["page-size","total","current-page"]),h("div",A,[o(E,{type:"danger",onClick:w,disabled:n.value.length===0},{default:p(()=>t[0]||(t[0]=[f(" 删除全部 ")])),_:1},8,["disabled"])])])])}}},F=M(G,[["__scopeId","data-v-fc6b6068"]]);export{F as default};