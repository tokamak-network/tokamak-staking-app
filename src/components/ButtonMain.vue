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
  width: 100%;
  height: 40px;
  border-radius: 4px;
  background-color: #257eee;
  display: flex;
  align-items: center;
  justify-content: center;
}

.text {
  color: white;
  font-size: 14px;
  font-weight: 500;  
}
</style>