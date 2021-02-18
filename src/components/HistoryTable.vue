<template>
  <view class="winner-table-container">
    <view class="table-header-container">
      <view class="th-name" :style="{ width: '25%' }">
        <text class="th-text">Tx Hash</text>
        </view>
      <view class="th-name" :style="{ width: '25%' }">
        <text class="th-text">Type</text></view
      >
      <view class="th-name" :style="{ width: '25%' }">
        <text class="th-text"
          >Amount <text :style="{ color: '#2a72e5' }">TON</text></text
        >
      </view>
      <view class="th-name" :style="{ width: '25%' }">
        <text class="th-text">Status</text></view
      >
    </view>
    <view class="table-content-container">
      <scroll-view-indicator
      :scrollIndicatorStyle='styles'
     :shouldIndicatorHide='false'
     :flexibleIndicator='false'
     :indicatorHeight='60' >
        <view class="list-row" v-for="transaction in orderedHistory" :key="transaction.transactionHash">
          <view class="list-item" :style="{ width: '25%' }">
          <touchable-opacity :on-press="()=>openWebView(transaction.transactionHash)">
              <text class="list-item-text list-item-text-tx">{{ transaction.transactionHash| hexSlicer  }}</text>
          </touchable-opacity>
          </view>
          <view class="list-item" :style="{ width: '25%', marginLeft: '2%' }">
            <text class="list-item-text">{{ transaction.type }}</text>
          </view>
          <view class="list-item" :style="{ width: '25%' }">
            <text class="list-item-text" :style="{ color: '#2a72e5' }">{{ currencyAmountFromNumberString(transaction.type, transaction.amount) }}</text>
          </view>
          <view class="list-item" :style="{ width: '25%' }">
            <text class="list-item-text">{{
            transaction.status
            }}</text>
          </view>
        </view>
      </scroll-view-indicator>
    </view>
    <tx-web-view :modalVisible="webViewVisible" :uri="uri" @propFromChild="childPropReceived"></tx-web-view>
  </view>
</template>
<script>
import { mapState } from "vuex";
import ScrollViewIndicator from "react-native-scroll-indicator";
import { orderBy } from 'lodash';
import TxWebView from "@/components/TxWebView"
import { getConfig } from "../../config.js"
export default {
  components: {
    "scroll-view-indicator": ScrollViewIndicator,
    "tx-web-view": TxWebView
  },
  data() {
    return {
      styles: {
        backgroundColor: "#5e94ea",
        opacity: 1,
      },
       orderedHistory: [],
       from: 'blockNumber',
       order: 'desc',
       webViewVisible: false,
       uri: ''
    };
  },
  computed: {
    ...mapState(["rounds", 'transactions']),
    toExplorer() {
      return (type, param) => this.$options.filters.toExplorer(type, param);
    },
     currencyAmountFromNumberString () {
      return (type, amount) => {
        if (type === 'Delegated') {
          return this.$options.filters.currencyAmountFromNumberString('TON', amount);
        } else {
          return this.$options.filters.currencyAmountFromNumberString('WTON', amount);
        }
      };
    },
    currencyAmount() {
      return (amount) => this.$options.filters.currencyAmount(amount);
    },
  },
  mounted () {
    this.orderedHistory = orderBy(this.transactions, (transaction) => transaction.blockNumber, [this.order]);
  },
  methods: {
   childPropReceived(args1) {
     this.webViewVisible = args1;
   },
   openWebView(args) {
     this.webViewVisible = true;
     this.uri = getConfig().prefixTransactionHash + args;
     console.log(this.uri)
   }
  }
};
</script>
<style scoped>
.winner-table-container {
  width: 90%;
  height: 81%;
}
.table-header-container {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  width: 100%;
  padding-left: 8px;
  margin-bottom: 15px;
}
.th-name {
  display: flex;
  /* justify-content: center; */
  align-self: stretch;
  align-items: center;
}
.th-text {
  font-size: 12px;
  font-weight: bold;
  color: #555555;
}
.list-row {
  display: flex;
  flex-direction: row;
}
.table-content-container {
  display: flex;
  flex-direction: row;
  background-color: #ffffff;
  border-width: 1px;
  border-radius: 10px;
  border-color: #e7ebf2;
  padding: 5px;
  padding-top: 20px;
}
.list-row {
  display: flex;

  flex-direction: row;
}
.list-item {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 9%;
}
.list-item-text {
  font-size: 11px;
}
.list-item-text-tx {
  text-decoration-line: underline;
}
</style>
