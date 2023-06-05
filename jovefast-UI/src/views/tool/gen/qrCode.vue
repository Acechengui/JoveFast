<template>
  <div>
    <div>
      <el-form
        ref="elForm"
        :model="formData"
        :rules="rules"
        size="medium"
        label-width="100px"
        label-position="right"
      >
        <el-row :gutter="15">
          <el-form-item label="二维码内容" prop="content">
            <el-input
              v-model="formData.content"
              type="textarea"
              placeholder="请输入二维码内容"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :style="{ width: '80%' }"
            ></el-input>
          </el-form-item>

          <el-form-item label="背景颜色">
            <el-color-picker
              v-model="formData.colorDark"
              size="medium"
            ></el-color-picker>
          </el-form-item>
        </el-row>
        <el-col :span="24">
          <el-row :gutter="15">
            <el-col :span="5">
              <el-form-item label="宽" prop="width">
                <!-- <el-input-number
                  v-model="formData.width"
                  placeholder="宽"
                ></el-input-number> -->
                <el-slider :max="1000" :min="50" v-model="formData.width"></el-slider>

              </el-form-item>
            </el-col>

            <el-col :span="5">
              <el-form-item label="高" prop="height">
                <!-- <el-input-number
                  v-model="formData.height"
                  placeholder="高"
                ></el-input-number> -->
                <el-slider :max="1000" :min="50" v-model="formData.height"></el-slider>
              </el-form-item>
            </el-col>
          </el-row>
        </el-col>

        <el-form-item size="large">
          <el-button type="primary" @click="submitForm">生成</el-button>
          <el-button @click="downLoad" type="info">立即下载</el-button>
          <el-button @click="resetForm" type="warning">{{ $t('common.reset') }}</el-button>
        </el-form-item>
        <el-row :gutter="15">
          <el-form-item>
            <!-- 二维码图片 -->
            <div id="qrCodeUrl"></div>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
// 安装 npm install qrcodejs2 --save
// 引入刚下载的qrcodejs2
import QRCode from "qrcodejs2";

export default {
  name: "qrCode",
  data() {
    return {
      formData: {
        content: null,
        width: 300,
        height: 300,
        colorDark: "#409EFF",
        colorLight: "#fff",
      },
      rules: {
        content: [
          {
            required: true,
            message: "请输入二维码内容",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {},
  methods: {
    submitForm() {
      this.$refs["elForm"].validate((valid) => {
        if (!valid) return;
        this.qrCodeGeneration();
      });
    },
    resetForm() {
      this.$refs["elForm"].resetFields();
      document.getElementById("qrCodeUrl").innerHTML = "";
    },
    // 二维码生成
    qrCodeGeneration() {
      this.$nextTick(function () {
        document.getElementById("qrCodeUrl").innerHTML = "";
        let qrCodeUrl = new QRCode("qrCodeUrl", {
          width: this.formData.width,
          height: this.formData.height,
          text: this.formData.content,
          colorDark: this.formData.colorDark,
          colorLight: this.formData.colorLight,
        });
      });
    },
    downLoad() {
         if(this.formData.content !=null){
            let myCanvas = document.getElementById('qrCodeUrl').getElementsByTagName('canvas');
                    let a = document.createElement('a')
                    a.href = myCanvas[0].toDataURL('image/png');
                    a.download = '二维码';
                    a.click()
                    this.$message({
                        message: "正在进行下载保存",
                        type: 'success'
                    })
         }  
    
    }
  },
};
</script>
 
<style scoped>
</style>