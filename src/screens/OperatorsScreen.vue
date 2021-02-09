<template>
    <view class="operator-layout" :style="{paddingTop: windowHeight*0.047}">
         <view class="page-container">
         <text class="page-title" :style="{marginBottom: windowHeight*0.008}">Select your favorite operator</text>
        <text class="page-text">Select an operator to stake, unstake, or </text>
        <text class="page-text" :style="{marginBottom: windowHeight*0.039}">withdraw your tokens.</text> 
         </view>
           <view class="operator-scroll" :style="{height: windowHeight * 0.77}">
        <scroll-view :showsVerticalScrollIndicator="false" >
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
import { Dimensions } from 'react-native';

export default {
    components: {
      'header': Header,
       'operator-component': OperatorComponent,
    },
    computed: {
    ...mapState([
      'operators',
    ]),
    windowWidth () {
      return Dimensions.get('window').width
    },
    windowHeight () {
      return Dimensions.get('window').height
    }
  },
    props: {
    navigation: {
      type: Object
    }, 
    pressedOperator: '', 
  },
  data() {
    return {
      index: -1
     }
  }, 
  methods: {
    selectOperator(op) {
     this.pressedOperator = op;
    }
  }
}
</script>
<style scoped>
.operator-layout {
  display: flex;
  flex-direction: column;
    background-color: #FAFBFC;

}
.operator-scroll  {
  display: flex;
  flex-direction: column;
  align-self: center;
  background-color: #FAFBFC;
  padding-bottom: 15%;
}
.page-container {
  
}
.page-title {
  font-size: 24px;
  text-align: center;
  font-weight: 700;
  color: #3e495c;
}
.page-text {
  font-size: 12px;
  text-align: center;
  color: #86929d;
}
</style>