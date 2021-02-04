<template>
     <view class="button-stake">
          <text class="text" >{{title}}</text>
        </view>
</template>
<script>
export default {
    props: ['title']
}
</script>
<style scoped>
.button-stake {
  background-color: #2a72e5;
  color: #ffffff;
  border-radius: 4px;
  width: 100%;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 700;
}

.text {
   color: white;
  font-size: 20px;
  font-weight: 700;  
}
</style>