import{_ as l,W as c,o as i,c as u,b as d,a9 as m}from"./index-BktgtzP8.js";const f=["xlink:href"],p={__name:"iconFromDIY",props:{name:{type:String,required:!0},className:{type:String,default:""}},setup(e){const s=e,n=c(()=>`${s.name}`),o=c(()=>s.className?`svg-icon ${s.className}`:"svg-icon");return(r,t)=>(i(),u("svg",{class:m(o.value),"aria-hidden":"true"},[d("use",{"xlink:href":n.value},null,8,f)],2))}},x=l(p,[["__scopeId","data-v-73ae75c5"]]),g=e=>{if(e==="")return;const s=1024,n=s*1024,o=n*1024,r=o*1024;let t="";e<.1*s?t=e.toFixed(2)+"B":e<.1*n?t=(e/s).toFixed(2)+"KB":e<o*.8?t=(e/n).toFixed(2)+"MB":e<r*.8?t=(e/o).toFixed(2)+"GB":t=(e/r).toFixed(2)+"TB";let a=t.indexOf(".");return t.substring(a+1,a+3)==="00"?t.substring(0,a)+t.substring(a+3,a+5):t};export{x as I,g as s};
