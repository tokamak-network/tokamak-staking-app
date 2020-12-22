<template>
    <view class="power-layout">
   <header :navigation="navigation" :back="false"/>
           <view class="power-container">
         <text>power TON</text>
      </view>
    </view>
</template>
<script>
import Header from '@/components/Header'
export default {
    components: {
      'header': Header,
    },
    props: {
    navigation: {
      type: Object
    }
  },
}
</script>
<style scoped>
.power-layout{ 
    display: flex;
  flex-direction: column;
  height: 100%;
}
.power-container {
 flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding-top: 40;
}
</style>