<template>
  <view class="winner-table-container">
    <view class="table-header-container">
      <view class="th-name" :style="{ width: '15%' }">
        <text class="th-text">Round</text></view
      >
      <view class="th-name" :style="{ width: '30%' }">
        <text class="th-text">Winner</text></view
      >
      <view class="th-name" :style="{ width: '25%' }">
        <text class="th-text"
          >Reward <text :style="{ color: '#2a72e5' }">TON</text></text
        >
      </view>
      <view class="th-name" :style="{ width: '30%' }">
        <text class="th-text">End date</text></view
      >
    </view>
    <view class="table-content-container">
      <scroll-view-indicator
        :scrollIndicatorStyle="styles"
        :shouldIndicatorHide="false"
        :flexibleIndicator="false"
        :indicatorHeight="60"
      >
        <view
          class="list-row"
          v-for="round in orderedRounds"
          :key="round.index"
        >
          <view class="list-item" :style="{ width: '15%' }">
            <text class="list-item-text">{{ round.index }}</text>
          </view>
          <view class="list-item" :style="{ width: '30%', marginLeft: '2%' }">
            <text class="list-item-text">{{ round.winner | hexSlicer }}</text>
          </view>
          <view class="list-item" :style="{ width: '25%' }">
            <text class="list-item-text" :style="{ color: '#2a72e5' }">{{
              currencyAmount(round.reward).substring(
                0,
                currencyAmount(round.reward).length - 3
              )
            }}</text>
          </view>
          <view class="list-item" :style="{ width: '30%' }">
            <text class="list-item-text">{{
              round.timestamp | formattedTimestamp
            }}</text>
          </view>
        </view>
      </scroll-view-indicator>
    </view>
  </view>
</template>
<script>
import { mapState } from "vuex";
import ScrollViewIndicator from "react-native-scroll-indicator";
import { orderBy } from "lodash";
export default {
  components: {
    "scroll-view-indicator": ScrollViewIndicator,
  },
  data() {
    return {
      styles: {
        backgroundColor: "#5e94ea",
        opacity: 1,
      },
      orderedRounds: [],
    };
  },
  computed: {
    ...mapState(["rounds"]),
    toExplorer() {
      return (type, param) => this.$options.filters.toExplorer(type, param);
    },
    formattedTimestamp() {
      return (timestamp) => this.$options.filters.formattedTimestamp(timestamp);
    },
    currencyAmount() {
      return (amount) => this.$options.filters.currencyAmount(amount);
    },
  },
  mounted() {
    this.orderedRounds = orderBy(this.rounds, (round) => round.index, "desc");
  },
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
</style>
