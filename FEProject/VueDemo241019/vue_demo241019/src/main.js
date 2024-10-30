import {createApp} from 'vue'
import App from './App.vue'
//导入
import globalComponent from './components/globalComponent.vue'

let app = createApp(App);
//注册
app.component("globalComponent", globalComponent);

app.mount('#app');
