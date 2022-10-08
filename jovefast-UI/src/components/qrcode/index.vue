<template>
  <main class="reader">
    <h1>拍照识别二维码</h1>
    <input type="file" @change="upload($event)" capture="camera" @input="$emit('input', $event.target.files[0])"/>
    <div>
      <img class="imgurl" :src="imgurl" alt="当前识别的二维码" />
    </div>
    <textarea
      class="result"
      cols="50"
      rows="3"
      v-model="result"
      placeholder="二维码识别结果"
      readonly
    ></textarea>
  </main>
</template>
<script>
export default {
  name: "Reader",
  data() {
    return {
      isok:false,//是否识别成功
      result: "",
      imgurl: "/static/img/logo.6da5b879.png"
    };
  },
  methods: {
      upload(e){
        const that= this;
        const file = e.target.files[0];
        const getObjectURL = window.createObjectURL ||  window.URL.createObjectURL || window.webkitURL.createObjectUR;
        this.imgurl = getObjectURL(file);
        const fReader = new FileReader();
        fReader.readAsDataURL(file);      
        fReader.onload = (e) => {
          this.imgurl = e.target.result;
          qrcode.decode(getObjectURL(file));
          qrcode.callback = function (imgMsg) {
            that.result=imgMsg;
            that.isok= imgMsg =='error decoding QR Code' ? false : true;
          }
        };

      }

  }
};
</script>
<style scoped>
.reader {
    margin-left: 10%;
}
.imgurl {
    width: 100px;
    height: 100px;
}
</style>
