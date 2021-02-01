<template>
    <view class="operator-layout">
         <view class="page-container">
         <text class="page-title">Select your favorite operator</text>
        <text class="page-text">Select an operator to stake, unstake, or </text>
        <text class="page-text">withdraw your tokens.</text> 
         </view>
           <view class="operator-scroll">
        <scroll-view>
        <view v-for="(operator, index) in operators" :key="index"> 
          <operator-component :layer2="operator.layer2" :navigation="navigation"/>
          </view>
          </scroll-view>
      </view>
    </view>
</template>
<script>
import Header from '../components/Header';
import OperatorComponent from '@/components/OperatorComponent';
import { mapState } from 'vuex';

export default {
    components: {
      'header': Header,
       'operator-component': OperatorComponent,
    },
    computed: {
    ...mapState([
      'operators',
    ]),
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
  padding-top: 30px
}
.operator-scroll  {
  display: flex;
  flex-direction: column;
  align-self: center;
  height: 87%;
}
.page-container {
  margin-bottom: 20px;
}
.page-title {
  font-size: 24px;
  text-align: center;
  font-weight: 900;
  color: #3e495c;
  margin-bottom: 5px;
}
.page-text {
  font-size: 12px;
  text-align: center;
  color: #86929d;
}
</style>