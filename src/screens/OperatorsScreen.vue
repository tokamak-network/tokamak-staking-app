<template>
    <view class="operator-layout">
   <header :navigation="navigation" :back="false"/>
         <view class="operator-container">
         <text class="page-title">Select your favorite operator!!</text>
        <text class="page-text">Select an operator to stake, unstake, or withdraw your tokens.</text>
          <scroll-view>
        <view v-for="operator in operators" :key="operator.name"> 
          <operator-component :name="operator.name" :color="operator.color" :navigation="navigation"/>
          </view>
          </scroll-view>
      </view>
    </view>
</template>
<script>
import Header from '../components/Header'
import OperatorComponent from '@/components/OperatorComponent'
export default {
    components: {
      'header': Header,
       'operator-component': OperatorComponent,
    },
   data() {
    return {
      operators: [
        {name:'tokamak1', color:'#b23756'},
        {name:'DXM Corp', color:'#8948a2'},
        {name:'DSRV', color:'#78eef2'}
        ]
    };
  },
    props: {
    navigation: {
      type: Object
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
  padding: 20px 20px;
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