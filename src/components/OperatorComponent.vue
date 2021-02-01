<template>
<view class="operator-wrap">
  <touchable-opacity  :on-press="() => selectOperator(operator.layer2)">
  <view class="operator-container">
     <image class="operator-img" :source="operator.name === 'tokamak1' ? tokamak : operator.name === 'DXM Corp' ? dxm : dsrv">
    </image>
     <view class="operator-text-container">
    <text class="operator-title">{{operator.name}}</text>
    <text :class="{'selected': pressed === true, 'operator-content': pressed !== true}">{{pressed === false ? info: 'More Information'}}</text>
    </view>
        
    </view>
  </touchable-opacity>
</view>
</template>
<script>
import { mapState, mapGetters } from 'vuex';
import { getConfig } from '../../config.js';
import tokamak from "../../assets/tokamak.png"
import dxm from "../../assets/dxm.png"
import dsrv from "../../assets/dsrv.png"
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
      data() {
    return {
      tokamak,
      dxm,
      dsrv,
      pressed: false
    }
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
   rateOf() {
      return (commissionRate) => this.$options.filters.rateOf(commissionRate);
    },
    fromNow() {
      return (timestamp, suffix = false) =>
        this.$options.filters.fromNow(timestamp, suffix);
    },
    info() {
      const commission = "Commission Rate: "
      const committed = "Last Committed: "
      const isNeg = this.operator.isCommissionRateNegative === 1 ? "-" : ""
      const rate = isNeg + this.rateOf(this.operator.commissionRate)
      const lastComm =  this.fromNow(this.operator.lastFinalizedAt)
      return (commission + rate + committed+ lastComm )
    }
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