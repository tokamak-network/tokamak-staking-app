<template>
    <view class="operator-layout">
       <header :openDrawer="openDr"/>
      <view class="operator-container">
         <text class="page-title">Stake tokens now!!!</text>
        <text class="page-text">Stake your TON to win Power TON and other rewards..</text>
        
      </view>
    <footer/>

    </view>
</template>
<script>
import Header from '../components/Header'
import Footer from '@/components/Footer'
export default {
    components: {
      'header': Header,
       'footer': Footer,
    },
    props: {
    navigation: {
      type: Object
    }
  },
    methods: {
      openDr (){
        this.navigation.openDrawer();
      }
    },
}
</script>
<style scoped>
.operator-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.operator-container {
  flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding-top: 40;
}
.page-title {
  font-size: 30px;
  text-align: center;
  color: #555555;
  padding: 0px 40px;
  font-weight: 700;
}
.page-text {
  font-size: 20px;
  text-align: center;
  padding: 20px 40px;
}
</style>