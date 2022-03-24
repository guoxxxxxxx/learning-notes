# Vue随记

### 1. 创建vue工程的具体步骤

```
// 1. 在终端窗口中输入
vue create project-name (输入工程名称时无法使用驼峰命名法)

// 2. 导入element-ui 在终端输入
npm i element-ui -S

// 3. 找到路径/src/main.js 并添加
-- 导入element-ui
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

// 4. 在Vue中安装
Vue.use(Element)
```





