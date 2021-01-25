import { cloneDeep, isEqual, range, uniq, orderBy } from "lodash";
import Vue from "vue-native-core";
import Vuex from "vuex";
import { NativeModules } from "react-native";
const { BlockchainModule } = NativeModules;
import { createCurrency } from "@makerdao/currency";
import web3EthABI from "web3-eth-abi";
import {
  getManagers,
  getOperators,
  getHistory,
  getTransactions,
  addTransaction,
} from "@/api";
import numeral from "numeral";
import { calculateExpectedSeig } from "tokamak-staking-lib";
import { BN, toBN } from "web3-utils";
import { acc } from "react-native-reanimated";

const _ETH = createCurrency("ETH");
const _TON = createCurrency("TON");
const _WTON = createCurrency("WTON");
const _POWER = createCurrency("POWER");
const TON_UNIT = "wei";
const WTON_UNIT = "ray";
Vue.use(Vuex);

const initialState = {
  loaded: false,
  signIn: false,
  user: "",
  blockNumber: 0,
  blockTimestamp: 0,

    // contract of managers
  TON: {},
  WTON: {},
  DepositManager: {},
  Layer2Registry: {},
  SeigManager: {},
  PowerTON: {},
  ethBalance: _ETH('0'),
  tonBalance: _TON('0'),
  wtonBalance: _WTON('0'),
  power: _POWER('0'),

  // operator
  operators: [],

  // round
  currentRound: {},
  rounds: [],

  // user transaction history
  history: [],

  // rank
  accountsDepositedWithPower: [],

  // not yet committed
  uncommittedCurrentRoundReward: _WTON('0'),

  TONbalance: {
    value: 1,
    symbol: " TON",
  },
  ETHbalance: {
    value: 1,
    symbol: " ETH",
  },
  powerTONbalance: {
    value: 100,
    symbol: " Power",
  },
  stakedAmount: {
    value: 6.77,
    symbol: " TON",
  },
  rewards: {
    value: 30,
    symbol: " TON",
  },
};
const getInitialState = () => initialState;

export default new Vuex.Store({
  state: cloneDeep(initialState),
  mutations: {
    SET_INITIAL_STATE: (state) => {
      const initialState = getInitialState();
      Object.keys(initialState).forEach((key) => {
        state[key] = initialState[key];
      });
    },
    SIGN_IN: (state, status) => {
      state.signIn = status;
    },
    SET_USER: (state, user) => {
      state.user = user;
    },
    SET_LOADING: (state, loaded) => {
      state.loaded = loaded;
    },
    SET_ETHBALACE: (state, balance) => {
      state.ethBalance = balance;
    },
    SET_TONBALACE: (state, balance) => {
      state.tonBalance = balance;
    },
    SET_WTON_BALANCE: (state, balance) => {
      state.wtonBalance = balance;
    },
    SET_BLOCK_NUMBER: (state, number) => {
      state.blockNumber = number;
    },
    SET_BLOCK_TIMESTAMP: (state, timestamp) => {
      state.blockTimestamp = timestamp;
    },
    SET_POWER: (state, power) => {
      state.power = power;
    },
    SET_OPERATORS: (state, operators) => {
      state.operators = operators;
    },
    SET_TRANSACTIONS: (state, transactions) => {
      state.transactions = transactions;
    },
    SET_MANAGERS: (state, managers) => {
      for (const [name, contract] of Object.entries(managers)) {
        state[name] = contract;
      }
    },
    UPDATE_OPERATOR: (state, newOperator) => {
      const index = state.operators.indexOf(prevOperator);
      const prevOperator = state.operators.find(operator => operator.layer2 === newOperator.layer2);
      for (const [key, value] of Object.entries(newOperator)) {
        prevOperator[key] = value;
      }
      Vue.set(state.operators, index, prevOperator);
    },
    SET_USER_HISTORY: (state, userHistory) => {
      state.userHistory = userHistory;
    },
    SET_CURRENT_ROUND: (state, round) => {
      state.currentRound = round;
    },
    SET_ROUNDS: (state, rounds) => {
      state.rounds = rounds;
    },
    SET_ACCOUNTS_DEPOSITED_WITH_POWER: (state, accounts) => {
      state.accountsDepositedWithPower = accounts;
    },
    ADD_ACCOUNT_DEPOSITED_WITH_POWER: (state, accountWithPower) => {
      const findAccount = (account) => account.address.toLowerCase() === accountWithPower.address.toLowerCase();
      const index = state.accountsDepositedWithPower.findIndex(findAccount);

      if (index > -1) {
        Vue.set(state.accountsDepositedWithPower, index, accountWithPower);
      } else {
        state.accountsDepositedWithPower.push(accountWithPower);
      }
    },
    SET_UNCOMMITTED_CURRENT_ROUND_REWARD: (state, reward) => {
      state.uncommittedCurrentRoundReward = reward;
    },
  },
  actions: {
    logout(context) {
      context.commit("SIGN_IN", false);
    },
    async signIn(context) {
      BlockchainModule.initis((init) => {
        if ((init = true)) {
          context.dispatch("setUser");
        }
      });
    await BlockchainModule.connect();
    },
    async setUser(context) {
      BlockchainModule.restoreAccs((result) => {
        if ((result = true)) {
          BlockchainModule.setAccountStatus((account) => {
            context.commit("SET_USER", account);
           context.dispatch('setEthBalance')
          });
        }
      });
    },
async setEthBalance (context) {
  BlockchainModule.getBalance((result) => {
    context.commit('SET_ETHBALACE', _ETH.wei(result.toString()));
    context.dispatch('set')
   
  })
},
async set (context) {
  const user = context.state.user;
  const blockNumber = await BlockchainModule.getBlockNumber();
  const blockTimestamp = await BlockchainModule.getTimeStamp(blockNumber);
  context.commit('SET_BLOCK_NUMBER', blockNumber);
  context.commit('SET_BLOCK_TIMESTAMP', blockTimestamp);
  const managers = await getManagers();
      const operators = await getOperators();
      const transactions = await getTransactions(user);
      await context.dispatch('setManagers', managers);
      await context.dispatch('setOperatorsWithRegistry', operators);
      await Promise.all([
         context.dispatch('setOperators', blockNumber),
        // context.dispatch('setBalance'),
        // context.dispatch('setCurrentRound'),
        // context.dispatch('setRounds'),
        // context.dispatch('setHistory'),
        // context.dispatch('setUncommittedCurrentRoundReward', blockNumber),
        // context.dispatch('checkPendingTransactions'),
      ]).catch(err => {
        // after logout, error can be happened
      });
},
    login(context) {
      context.commit("SIGN_IN", true);
    },
    setManagers(context, managers) {
      for (const [name, address] of Object.entries(managers)) {
        managers[name] = address;
      }
      context.commit('SET_MANAGERS', managers);
     
    },
    setOperatorsWithRegistry(context,operators) {
      context.commit('SET_OPERATORS', operators);
      context.commit("SET_LOADING", true);
    },
   async setOperators (context, blockNumber){
      const user = context.state.user;

      const TON = context.state.TON;
      const WTON = context.state.WTON;
      // const web3 = context.state.web3;
      const DepositManager = context.state.DepositManager;
      const SeigManager = context.state.SeigManager;
      const l2Registry = context.state.Layer2Registry;
       const Tot = await BlockchainModule.callMethod('tot',SeigManager, "0");
       console.log(Tot);
    },
  },

  getters: {},
});
