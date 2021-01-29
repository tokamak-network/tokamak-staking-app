<template>
<view class="operator-container">
  <touchable-opacity  :on-press="() => selectOperator(operator.layer2)">
 <view class="row" style="flex-direction: row">
        <text class="title">{{operator.name }}</text>
        <view class="avatar" :style="{backgroundColor: operator.color }">
          <text class="opr">O P R</text>
        </view>
    </view>
  </touchable-opacity>
</view>
</template>
<script>
import { mapState, mapGetters } from 'vuex';
import { getConfig } from '../../config.js';

export default {
     props: {
       layer2: {
      required: true,
      type: String,
    },
       navigation: {
      type: Object
    },
     },
     computed: {
    ...mapState([
      'user',
      'DepositManager',
    ]),
    ...mapGetters([
      'operatorByLayer2',
    ]),
    operator () {
      return this.operatorByLayer2(this.layer2);
    },
   
  },
     methods: {
       selectOperator (operator) {
        const nlayer2 = operator.toLowerCase();
         this.navigation.push('SelectedOperator', {layer2: nlayer2} );
       }
     }
}
</script>
<style scoped>
.operator-container {
   display: flex;
    width: 350;
    background-color: #e2e8eb;
    border-width: 1;
    border-color: #ccd1d3;
    border-radius: 13;
  margin-bottom: 20;
  padding: 10px;
}
.row {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  align-self: stretch;
  align-items: center;
  position: relative;
}
.title {
    color: #8c8c8c;
    font-size: 18px;  
}
.text {
   font-size: 40px;
  font-weight: 700;
  color: #555555; 
}
.avatar {
  display: flex;
  width: 65px;
  height: 65px;
  font-size: 30px;
   font-weight: 700;
  background-color: #555555;
  border-radius: 50;
  justify-content: center;
  align-self: stretch;
  align-items: center;
  position: relative;
}
.opr{
  color: white;
  font-size: 20px;
  font-weight: 600;
}
</style>