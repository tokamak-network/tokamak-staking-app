<template>
    <view class="operator-layout">
        <view class="page-container">
        <text class="page-title">Select your favorite operator</text>
        <text class="page-text">Select an operator to stake, unstake, or </text>
        <text class="page-text">withdraw your tokens.</text>
        </view>
        <view class="operator-scroll">
        <scroll-view>
          <view v-for="operator in operators" :key="operator.name"> 
            <operator-component :name="operator.name" :content="operator.content" :navigation="navigation"/>
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
       'operator-component': OperatorComponent
    },
   data() {
    return {
      operators: [
        {name:'Tokamak1', content: "Commission Rate 2.5% , 20 hours ago"},
        {name:'DXM Corp', content: "Commission Rate 0% , 2 day ago"},
        {name:'DSRV', content: "Commission Rate 0% , 2 day ago"}
        ],
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
  padding-top: 4.7%
}

.operator-scroll  {
  display: flex;
  flex-direction: column;
}

.page-container {
  margin-bottom: 3.9%;
}

.page-title {
  font-size: 24px;
  text-align: center;
  font-weight: bold;
  color: #3e495c;
  margin-bottom: 0.8%;
}
.page-text {
  font-size: 12px;
  text-align: center;
  color: #86929d;
}
</style>