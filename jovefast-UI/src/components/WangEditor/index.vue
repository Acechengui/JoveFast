<template>
  <div>
    <div style="border: 1px solid #ccc; margin-top: 10px">
      <!-- 工具栏 -->
      <Toolbar
        style="border-bottom: 1px solid #ccc"
        :editor="editor"
        :defaultConfig="toolbarConfig"
      />
      <!-- 编辑器 -->
      <Editor
        style="height: 400px; overflow-y: hidden"
        :defaultConfig="editorConfig"
        v-model="html"
        @onChange="onChange"
        @onCreated="onCreated"
      />
    </div>
  </div>
</template>
  
  <script>
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { getToken } from '@/utils/auth'
export default {
  name: "WangEditor",
  components: { Editor, Toolbar },
  data() {
    return {
      filePath: "", //请求路径
      editor: null,
      html: "",
      toolbarConfig: {
        // toolbarKeys: [ /* 显示哪些菜单，如何排序、分组 */ ],
        // excludeKeys: [ /* 隐藏哪些菜单 */ ],
      },
      editorConfig: {
        placeholder: "请输入内容...",
        // autoFocus: false,
        // 所有的菜单配置，都要在 MENU_CONF 属性下
        MENU_CONF: {
          server: process.env.VUE_APP_BASE_API + this.filePath, // 上传的图片服务器地址
          // form-data fieldName ，默认值 'wangeditor-uploaded-image'
          fieldName: "wangeditorUploadedImage",
          // 单个文件的最大体积限制，默认为 2M
          maxFileSize: 10 * 1024 * 1024, // 10M
          // 最多可上传几个文件，默认为 100
          maxNumberOfFiles: 10,
          // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
          allowedFileTypes: ["image/*"],
          // 自定义上传参数，例如传递验证的 token 等。参数会被添加到 formData 中，一起上传到服务端。
          meta: {
            token: getToken(),
          },
          // 将 meta 拼接到 url 参数中，默认 false
          metaWithUrl: false,
          // 自定义增加 http  header
          headers: {
            Accept: "text/x-json",
          },
          // 跨域是否传递 cookie ，默认为 false
          withCredentials: false,
          // 超时时间，默认为 10 秒
          timeout: 10 * 1000, // 10 秒
        },
      },
    };
  },
  methods: {
    onCreated(editor) {
      this.editor = Object.seal(editor); // 【注意】一定要用 Object.seal() 否则会报错
    },
    onChange(editor) {
      console.log("onChange", editor.getHtml()); // onChange 时获取编辑器最新内容
    },
    getEditorText() {
      const editor = this.editor;
      if (editor == null) return;
      console.log(editor.getText()); // 执行 editor API
    },
    printEditorHtml() {
      const editor = this.editor;
      if (editor == null) return;
      console.log(editor.getHtml()); // 执行 editor API
    },
  },
  mounted() {
    // 模拟 ajax 请求，异步渲染编辑器
    // setTimeout(() => {
    //   this.html = "<p>Ajax 异步设置内容 HTML</p>";
    // }, 1500);
  },
  beforeDestroy() {
    const editor = this.editor;
    if (editor == null) return;
    editor.destroy(); // 组件销毁时，及时销毁 editor ，重要！！！
  },
};
</script>
  
  <style src="@wangeditor/editor/dist/css/style.css"></style>
  