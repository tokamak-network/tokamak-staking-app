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
import { BN,isBN, toBN, toChecksumAddress } from "web3-utils";
import { acc } from "react-native-reanimated";
import BigNumber from "bignumber.js";
import bigInt from "big-integer";

import PowerTONABI from '@/contracts/abi/PowerTON.json';

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
  transactions: [],
  pendingTransactions: [],
  // contract of managers
  TON: {},
  WTON: {},
  DepositManager: {},
  Layer2Registry: {},
  SeigManager: {},
  PowerTON: {},
  ethBalance: _ETH("0"),
  tonBalance: _TON("0"),
  wtonBalance: _WTON("0"),
  power: _POWER("0"),

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
  uncommittedCurrentRoundReward: _WTON("0"),
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
  selectedOperator: '',
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
    ADD_TRANSACTION: (state, newTransaction) => {
      if (!state.transactions.find(transaction => transaction.transactionHash === newTransaction.transactionHash)) {
        state.transactions.push(newTransaction);
      }
    },
    SET_PENDING_TRANSACTIONS: (state, pendingTransactions) => {
      state.pendingTransactions = pendingTransactions;
    },
    ADD_PENDING_TRANSACTION: (state, newPendingTransaction) => {
      if (!state.pendingTransactions.find(pendingTransaction => pendingTransaction.transactionHash === newPendingTransaction.transactionHash)) {
        state.pendingTransactions.push(newPendingTransaction);
      }
      setPendingTransactions(state.pendingTransactions);
    },
    DELETE_PENDING_TRANSACTION: (state, minedTransaction) => {
      state.pendingTransactions.splice(state.pendingTransactions.map(pendingTransaction => pendingTransaction.transactionHash).indexOf(minedTransaction.transactionHash), 1);
      setPendingTransactions(state.pendingTransactions);
    },
    SET_MANAGERS: (state, managers) => {
      for (const [name, contract] of Object.entries(managers)) {
        state[name] = contract;
      }
    },
    UPDATE_OPERATOR: (state, newOperator) => {
      const index = state.operators.indexOf(prevOperator);
      const prevOperator = state.operators.find(
        (operator) => operator.layer2 === newOperator.layer2
      );
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
      const findAccount = (account) =>
        account.address.toLowerCase() ===
        accountWithPower.address.toLowerCase();
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

    SET_SELECTED_OPERATOR: (state, operator) => {
      state.selectedOperator = operator
    }
  },
  actions: {
    logout(context) {
      context.commit('SET_INITIAL_STATE');
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
            context.dispatch("setEthBalance");
          });
        }
      });
    },
    async setEthBalance(context) {
      const ethBalance = await BlockchainModule.getBalance();
      context.commit("SET_ETHBALACE", _ETH.wei(ethBalance.toString()));
      context.dispatch("set");
    },
    async set(context) {
      const user = context.state.user;
      const blockNumber = await BlockchainModule.getBlockNumber();
      const blockTimestamp = await BlockchainModule.getTimeStamp(blockNumber);
      context.commit("SET_BLOCK_NUMBER", blockNumber);
      context.commit("SET_BLOCK_TIMESTAMP", blockTimestamp);
      const managers = await getManagers();
      const operators = await getOperators();
      const transactions = await getTransactions(user);
      
      await Promise.all([
         context.dispatch("setManagers", managers),
       context.dispatch("setOperatorsWithRegistry", operators),
        context.dispatch('setTransactionsAndPendingTransactions', transactions),
        context.dispatch("setOperators", blockNumber),
        context.dispatch('setBalance'),
        context.dispatch('setCurrentRound'),
        context.dispatch('setRounds'),
        context.dispatch('setHistory'),
        // context.dispatch('checkPendingTransactions'),
      ]).catch((err) => {
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
      context.commit("SET_MANAGERS", managers);
    },
    setOperatorsWithRegistry(context, operators) {
      context.commit("SET_OPERATORS", operators);
    },async setTransactionsAndPendingTransactions (context, transactions) {
      context.commit('SET_TRANSACTIONS', transactions);
      const pendingTransactions = getPendingTransactions();
      context.commit('SET_PENDING_TRANSACTIONS', pendingTransactions);
    },
    async addPendingTransaction (context, transaction) {
      context.commit('ADD_PENDING_TRANSACTION', transaction);
    },
    async deletePendingTransaction (context, transaction) {
      context.commit('DELETE_PENDING_TRANSACTION', transaction);
    },
    async setOperators(context, blockNumber) {
      const user = context.state.user;

      const TON = context.state.TON;
      const WTON = context.state.WTON;
      // const web3 = context.state.web3;
      const DepositManager = context.state.DepositManager;
      const SeigManager = context.state.SeigManager;
      const l2Registry = context.state.Layer2Registry;
      const tot = await BlockchainModule.callMethod("tot", SeigManager, "");
      const Tot = toChecksumAddress(tot.substring(0, 2) + tot.substring(26));
      const [tonTotalSup, totTotalSup, tonBalanceWTON] = await Promise.all([
        BlockchainModule.callMethod("totalSupply", TON, ""),
        BlockchainModule.callMethod("totalSupply", Tot, ""),
        BlockchainModule.callMethod("balanceOf", TON, WTON),
      ]);
      const tonTotalSupply = bigInt(parseInt(tonTotalSup)).toString();
      const totTotalSupply = bigInt(parseInt(totTotalSup)).toString();
      const tonBalanceOfWTON = bigInt(parseInt(tonBalanceWTON)).toString();

      const operators = context.state.operators.slice(0, 5);
      for (let i = 0; i < operators.length; i++) {
        const result = await BlockchainModule.callMethod(
          "layer2s",
          l2Registry,
          operators[i].layer2
        );
        if (parseInt(result) !== 1) {
          operators.splice(i, 1);
        }
      }
      const operatorsFromLayer2 = await Promise.all(
        operators.map(async (operatorFromLayer2) => {
          
          const layer2 = operatorFromLayer2.layer2;
          const coinage = await BlockchainModule.callMethod(
            "coinages",
            SeigManager,
            layer2
          );
          const Coinage = toChecksumAddress(
            coinage.substring(0, 2) + coinage.substring(26)
          );
          const [op, currentForks] = await Promise.all([
            BlockchainModule.callMethod("operator", layer2, ""),
            BlockchainModule.callMethod("currentFork", layer2, ""),
          ]);
          const operator = toChecksumAddress(
            op.substring(0, 2) + op.substring(26)
          );
          const currentForkNumber = bigInt(parseInt(currentForks)).toString();
          
          const getRecentCommit = async (operator, layer2) => {
            const commitTransactions = [];
            const blockNumbers = [];
            const transactions = await getTransactions(operator);
            for (const transaction of transactions) {
              if (
                transaction.type === "Commit" &&
                transaction.target === layer2
              ) {
                commitTransactions.push(transaction);
                blockNumbers.push(transaction.blockNumber);
              }
            }
            if (blockNumbers.length === 0) {
              return ["0", "1"];
            } else {
              // const blockNumber = Math.max.apply(null, blockNumbers);
              const blockNumb = await BlockchainModule.callMethod(
                "lastCommitBlock",
                SeigManager,
                layer2
              );
              const blockNumber = parseInt(blockNumb);

              const blockTimestamp = await BlockchainModule.getTimeStamp(
                blockNumber
              );
              return [String(blockTimestamp), String(blockNumbers.length + 1)];
            }
          };
          const getLastFinalizedAt = async (lastFinalizedEpochNumber,lastFinalizedBlockNumber) => {
            const epo = await BlockchainModule.callSmartMethod(
              "getEpoch",
              layer2,
              currentForkNumber,
              lastFinalizedEpochNumber
            );
            const isEmpty = bigInt(
              parseInt("0x" + epo.substring(194, 258))
            ).toString();
            const initialized = bigInt(
              parseInt("0x" + epo.substring(258, 322))
            ).toString();
            const isRequest = bigInt(
              parseInt("0x" + epo.substring(322, 386))
            ).toString();
            const userActivated = bigInt(
              parseInt("0x" + epo.substring(386, 450))
            ).toString();
            const rebase = bigInt(
              parseInt("0x" + epo.substring(450, 514))
            ).toString();

            const epoch = {};
            epoch.startBlockNumber = bigInt(
              parseInt("0x" + epo.substring(2, 66))
            ).toString();
            epoch.endBlockNumber = bigInt(
              parseInt("0x" + epo.substring(66, 130))
            ).toString();
            epoch.timestamp = bigInt(
              parseInt("0x" + epo.substring(130, 194))
            ).toString();
            epoch.isEmpty = isEmpty === "0" ? false : true;
            epoch.initialized = initialized === "0" ? false : true;
            epoch.isRequest = isRequest === "0" ? false : true;
            epoch.userActivated = userActivated === "0" ? false : true;
            epoch.rebase = rebase === "0" ? false : true;
            epoch.RE = {};
            epoch.RE.requestStart = bigInt(
              parseInt("0x" + epo.substring(514, 578))
            ).toString();
            epoch.RE.requestEnd = bigInt(
              parseInt("0x" + epo.substring(578, 642))
            ).toString();
            epoch.RE.firstRequestBlockId = bigInt(
              parseInt("0x" + epo.substring(642, 706))
            ).toString();
            epoch.RE.numEnter = bigInt(
              parseInt("0x" + epo.substring(706, 770))
            ).toString();
            epoch.RE.nextEnterEpoch = bigInt(
              parseInt("0x" + epo.substring(770, 834))
            ).toString();
            epoch.RE.nextEpoch = bigInt(
              parseInt("0x" + epo.substring(834, 898))
            ).toString();
            epoch.NRE = {};
            epoch.NRE.epochStateRoot = "0x" + epo.substring(898, 962);
            epoch.NRE.epochTransactionsRoot = "0x" + epo.substring(962, 1026);
            epoch.NRE.epochReceiptsRoot = "0x" + epo.substring(1026, 1090);
            epoch.NRE.submittedAt = bigInt(
              parseInt("0x" + epo.substring(1090, 1154))
            ).toString();
            epoch.NRE.finalizedAt = bigInt(
              parseInt("0x" + epo.substring(1154, 1218))
            ).toString();
            epoch.NRE.finalized = bigInt(
              parseInt("0x" + epo.substring(1218, 1282))
            ).toString();
            epoch.NRE.challenging = bigInt(
              parseInt("0x" + epo.substring(1282, 1346))
            ).toString();
            epoch.NRE.challenged = bigInt(
              parseInt("0x" + epo.substring(1346, 1410))
            ).toString();

            const block = await BlockchainModule.callSmartMethod(
              "getBlock",
              layer2,
              currentForkNumber,
              lastFinalizedBlockNumber
            );
            const finalizedAt = bigInt(
              parseInt("0x" + block.substring(194, 258))
            ).toString();
            const timestamp = epoch.isRequest
              ? finalizedAt
              : epoch.NRE.finalizedAt;
            return timestamp;
          };
          
          const getDeposit = async (account) => {
            let accountStaked, accountUnstaked;
            if (typeof account === "undefined") {
              accountStaked = await BlockchainModule.callMethod("accStakedLayer2",DepositManager,layer2);
              accountUnstaked = await BlockchainModule.callMethod("accUnstakedLayer2",DepositManager,layer2 )
            }
            else {
              accountStaked = await BlockchainModule.callSmartMethod(
                "accStaked",
                DepositManager,
                layer2,
                account
              );
              accountUnstaked = await BlockchainModule.callSmartMethod(
                "accUnstaked",
                DepositManager,
                layer2,
                account
              );
            } 
            const accStaked = new BN(bigInt(parseInt(accountStaked)).toString());
            const accUnstaked = new BN(bigInt(parseInt(accountUnstaked)).toString());
           const deposit = accStaked.sub(accUnstaked);
            if (deposit.cmp(new BN('0')) === -1) { // https://github.com/Onther-Tech/plasma-evm-contracts/issues/39
              return '0';
            } else {
              return deposit.toString();
            }
          };
         const getPendingRequests = async () => {
          const numPendingRequests = await BlockchainModule.callSmartMethod('numPendingRequests', DepositManager, layer2, user);
          if (parseInt(numPendingRequests) === 0) {
            return [];
          }
          const requestIndex = await BlockchainModule.callSmartMethod('withdrawalRequestIndex', DepositManager, layer2, user );
        
          let index = parseInt(requestIndex);
         
          const pendingRequests = [];
          for (const _ of range(numPendingRequests)) {
            const req = await BlockchainModule.callSmartFunc('withdrawalRequest', DepositManager, layer2, user, index.toString() );
            const request = {}; 
            request.withdrawableBlockNumber =  bigInt(parseInt("0x" + req.substring(2, 66))).toString();
            request.amount = bigInt(parseInt("0x" + req.substring(66, 130))).toString();
            request.processed = bigInt(parseInt("0x" + req.substring(130, 194))).toString();
            pendingRequests.push(request);
            index++;
          }
          return Promise.all(pendingRequests);
         }
         const filterNotWithdrawableRequests = (requests) => {
          return requests.filter(request => parseInt(request.withdrawableBlockNumber) > blockNumber);
        };
        const filterWithdrawableRequests = (requests) => {
          return requests.filter(request => parseInt(request.withdrawableBlockNumber) <= blockNumber);
        };
        const getUserNotWithdrawable = (notWithdrawableRequests) => {
          const initialAmount = _WTON.ray('0');
          const reducer = (amount, request) => amount.add(_WTON.ray(request.amount));
          return notWithdrawableRequests.reduce(reducer, initialAmount);
        };

        const getUserWithdrawable = (withdrawableRequests) => {
          const initialAmount = _WTON.ray('0');
          const reducer = (amount, request) => amount.add(_WTON.ray(request.amount));
          return withdrawableRequests.reduce(reducer, initialAmount);
        };
        const getExpectedSeigs = async () => {
          const [
            isRateNegative,
            ispaused,
            lastSeig,
            unpaused,
            pausedblk,
          ] = await Promise.all([
            BlockchainModule.callMethod('isCommissionRateNegative', SeigManager, layer2),
            BlockchainModule.callMethod('paused', SeigManager, ""),
            BlockchainModule.callMethod('lastSeigBlock', SeigManager, ""), 
            BlockchainModule.callMethod('unpausedBlock', SeigManager, ""),
            BlockchainModule.callMethod('pausedBlock', SeigManager, ""),
          ]); 
          const isCommissionRateNegative = bigInt(parseInt(isRateNegative)).toString() === "" ? false : true;
           const paused =   bigInt(parseInt(ispaused)).toString() === "" ? false : true;
           const lastSeigBlock =  bigInt(parseInt(lastSeig)).toString();
           const unpausedBlock = bigInt(parseInt(unpaused)).toString();
           const pausedBlock = bigInt(parseInt(pausedblk)).toString();
         
           let [
            seigPerBlk,
            commissionRte,
            prevTotTotalSup,
            prevTotBal,
            prevCoinageTotalSup,
            prevCoinageOperatorBal,
            prevCoinageUserBal,
          ] = await Promise.all([
            BlockchainModule.callMethod('seigPerBlock', SeigManager, ""),
            BlockchainModule.callMethod('commissionRates', SeigManager,layer2),
            BlockchainModule.callMethod('totalSupply', Tot, ""),
            BlockchainModule.callMethod('balanceOf', Tot, layer2),
            BlockchainModule.callMethod('totalSupply', Coinage, ""),
            BlockchainModule.callMethod('balanceOf', Coinage, operator),
            BlockchainModule.callMethod('balanceOf', Coinage, user),
          ]);
         
          const seigPerBlock = _WTON( bigInt(parseInt(seigPerBlk)).toString(), WTON_UNIT);
           const commissionRate = _WTON(bigInt(parseInt(commissionRte)).toString(), WTON_UNIT);
           const prevTotTotalSupply = _WTON(bigInt(parseInt(prevTotTotalSup)).toString(), WTON_UNIT);
          const prevTotBalance = _WTON( bigInt(parseInt(prevTotBal)).toString(), WTON_UNIT);
          const prevCoinageTotalSupply =  _WTON( bigInt(parseInt(prevCoinageTotalSup)).toString(), WTON_UNIT);
          const  prevCoinageOperatorBalance = _WTON( bigInt(parseInt(prevCoinageOperatorBal)).toString(), WTON_UNIT);
           const prevCoinageUserBalance =  _WTON(bigInt(parseInt(prevCoinageUserBal)).toString(), WTON_UNIT); 
           const prevCoinageUsersBalance = prevCoinageTotalSupply.minus(prevCoinageOperatorBalance);
           
           function calcNumSeigBlocks () {
            if (paused) return 0;

            const span = blockNumber - lastSeigBlock + 1; // + 1 for new block
           
            if (unpausedBlock < lastSeigBlock) {
              return span;
            }

            return span - (unpausedBlock - pausedBlock);
          }
          function increaseTot () {
            const maxSeig = seigPerBlock.times(calcNumSeigBlocks());
            const tos = _WTON(tonTotalSupply, TON_UNIT)
              .plus(_WTON(totTotalSupply, WTON_UNIT))
              .minus(_WTON(tonBalanceOfWTON, TON_UNIT));

            const stakedSeigs = maxSeig.times(prevTotTotalSupply).div(tos);
           return stakedSeigs;
          }

          const stakedSeigs = increaseTot();
            let layer2Seigs, operatorSeigs, usersSeigs;
            if (prevTotTotalSupply.isEqual(_WTON('0'))) {
              layer2Seigs = _WTON('0', WTON_UNIT);
            } else {
              layer2Seigs = stakedSeigs.times(prevTotBalance).div(prevTotTotalSupply);
            }

            if (prevCoinageTotalSupply.isEqual(_WTON('0'))) {
              operatorSeigs = _WTON('0', WTON_UNIT);
              usersSeigs = _WTON('0', WTON_UNIT);
            } else {
              operatorSeigs = layer2Seigs.times(prevCoinageOperatorBalance).div(prevCoinageTotalSupply);
              usersSeigs = layer2Seigs.times(prevCoinageUsersBalance).div(prevCoinageTotalSupply);
            }
            function _calcSeigsDistribution () {
              let operatorSeigsWithCommissionRate = operatorSeigs;
              let usersSeigsWithCommissionRate = usersSeigs;

              if (commissionRate.toFixed(WTON_UNIT) === '0') {
                return {
                  operatorSeigsWithCommissionRate,
                  usersSeigsWithCommissionRate,
                };
              }

              if (!isCommissionRateNegative) {
                const commissionFromUsers = usersSeigs.times(commissionRate);

                operatorSeigsWithCommissionRate = operatorSeigsWithCommissionRate.plus(commissionFromUsers);
                usersSeigsWithCommissionRate = usersSeigsWithCommissionRate.minus(commissionFromUsers);
                return {
                  operatorSeigsWithCommissionRate,
                  usersSeigsWithCommissionRate,
                };
              }

              if (prevCoinageTotalSupply.toFixed(WTON_UNIT) === '0' ||
                prevCoinageOperatorBalance.toFixed(WTON_UNIT) === '0') {
                return {
                  operatorSeigsWithCommissionRate,
                  usersSeigsWithCommissionRate,
                };
              }

              const commissionFromOperator = operatorSeigs.times(commissionRate);

              operatorSeigsWithCommissionRate = operatorSeigsWithCommissionRate.minus(commissionFromOperator);
              usersSeigsWithCommissionRate = usersSeigsWithCommissionRate.plus(commissionFromOperator);

              return {
                operatorSeigsWithCommissionRate,
                usersSeigsWithCommissionRate,
              };
            }
            const {
              operatorSeigsWithCommissionRate,
              usersSeigsWithCommissionRate,
            } = _calcSeigsDistribution();
            let userSeigsWithCommissionRate;
            if (prevCoinageUsersBalance.isEqual(_WTON('0', WTON_UNIT))) {
              userSeigsWithCommissionRate = _WTON('0', WTON_UNIT);
            } else {
              userSeigsWithCommissionRate = usersSeigsWithCommissionRate.times(prevCoinageUserBalance).div(prevCoinageUsersBalance);
            }

            return {
              operatorSeigs: operatorSeigsWithCommissionRate,
              userSeigs: userSeigsWithCommissionRate,
              layer2Seigs: layer2Seigs,
            }; 
        }
        const [
          currentfork,
          firstEpo,
          totalDeposit,
          selfDeposit,
          userDeposit,
          totStaked,
          selfStake,
          userStake,
          pendingRequests,
          seigs, // operatorSeigs, userSeigs, layer2Seigs
          CommissionRateNegative,
          commRate,
          powerTONSeigRt,
          daoSeigRt,
          relativeSeigRt,
          delayedCommissionRateNeg,
          delayedCommissionRt,
          delayedCommBlock,
          withdrawDelay,
          globalWithdrawDelay,
          minAmount
        ] = await Promise.all([
          BlockchainModule.callMethod("forks", layer2, currentForkNumber.toString()),
          BlockchainModule.callSmartMethod( "getEpoch",layer2, "0", "0" ),
          getDeposit(),
          getDeposit(operator),
          getDeposit(user),
          BlockchainModule.callMethod("totalSupply", Coinage,""),
          BlockchainModule.callMethod("balanceOf", Coinage, operator),
          BlockchainModule.callMethod("balanceOf", Coinage, user),
          getPendingRequests(),
          getExpectedSeigs(),
          BlockchainModule.callMethod("isCommissionRateNegative", SeigManager,layer2),
          BlockchainModule.callMethod("commissionRates", SeigManager,layer2),
          BlockchainModule.callMethod("powerTONSeigRate", SeigManager,""),
          BlockchainModule.callMethod("daoSeigRate", SeigManager,""),
          BlockchainModule.callMethod("relativeSeigRate", SeigManager,""),
          BlockchainModule.callMethod("delayedCommissionRateNegative", SeigManager,layer2),
          BlockchainModule.callMethod("delayedCommissionRate", SeigManager,layer2),        
          BlockchainModule.callMethod("delayedCommissionBlock", SeigManager,layer2), 
          BlockchainModule.callMethod("withdrawalDelay", DepositManager,layer2),   
          BlockchainModule.callMethod("globalWithdrawalDelay", DepositManager,""),  
          BlockchainModule.callMethod("minimumAmount", SeigManager,""),  
        ]);
        currentFork = {};
        currentFork.forkedBlock = bigInt( parseInt("0x" + currentfork.substring(2, 66))).toString();
        currentFork.firstEpoch = bigInt( parseInt("0x" + currentfork.substring(66, 130))).toString();
        currentFork.lastEpoch = bigInt( parseInt("0x" + currentfork.substring(130, 194))).toString();
        currentFork.firstBlock = bigInt( parseInt("0x" + currentfork.substring(194, 258))).toString();
        currentFork.lastBlock = bigInt( parseInt("0x" + currentfork.substring(258, 322))).toString();
        currentFork.lastFinalizedEpoch = bigInt( parseInt("0x" + currentfork.substring(322, 386))).toString();
        currentFork.lastFinalizedBlock = bigInt( parseInt("0x" + currentfork.substring(386, 450))).toString();
        currentFork.timestamp = bigInt( parseInt("0x" + currentfork.substring(450, 514))).toString();
        currentFork.firstEnterEpoch = bigInt( parseInt("0x" + currentfork.substring(514, 578))).toString();
        currentFork.lastEnterEpoch = bigInt( parseInt("0x" + currentfork.substring(578, 642))).toString();
        currentFork.nextBlockToRebase = bigInt( parseInt("0x" + currentfork.substring(642, 706))).toString();
        currentFork.rebased = bigInt( parseInt("0x" + currentfork.substring(706,770))).toString();

        const isEmpty = bigInt(parseInt("0x" + firstEpo.substring(194, 258))).toString();
        const initialized = bigInt(parseInt("0x" + firstEpo.substring(258, 322))).toString();
        const isRequest = bigInt(parseInt("0x" + firstEpo.substring(322, 386))).toString();
        const userActivated = bigInt(parseInt("0x" + firstEpo.substring(386, 450))).toString();
        const rebase = bigInt(parseInt("0x" + firstEpo.substring(450, 514))).toString();

        const firstEpoch = {};
        firstEpoch.startBlockNumber = bigInt(parseInt("0x" + firstEpo.substring(2, 66))).toString();
        firstEpoch.endBlockNumber = bigInt(parseInt("0x" + firstEpo.substring(66, 130))).toString();
        firstEpoch.timestamp = bigInt(parseInt("0x" + firstEpo.substring(130, 194))).toString();
        firstEpoch.isEmpty = isEmpty === "0" ? false : true;
        firstEpoch.initialized = initialized === "0" ? false : true;
        firstEpoch.isRequest = isRequest === "0" ? false : true;
        firstEpoch.userActivated = userActivated === "0" ? false : true;
        firstEpoch.rebase = rebase === "0" ? false : true;
        firstEpoch.RE = {};
        firstEpoch.RE.requestStart = bigInt(parseInt("0x" + firstEpo.substring(514, 578))).toString();
        firstEpoch.RE.requestEnd = bigInt(parseInt("0x" + firstEpo.substring(578, 642))).toString();
        firstEpoch.RE.firstRequestBlockId = bigInt(parseInt("0x" + firstEpo.substring(642, 706))).toString();
        firstEpoch.RE.numEnter = bigInt(parseInt("0x" + firstEpo.substring(706, 770))).toString();
        firstEpoch.RE.nextEnterEpoch = bigInt(parseInt("0x" + firstEpo.substring(770, 834))).toString();
        firstEpoch.RE.nextEpoch = bigInt(parseInt("0x" + firstEpo.substring(834, 898))).toString();
        firstEpoch.NRE = {};
        firstEpoch.NRE.epochStateRoot = "0x" + firstEpo.substring(898, 962);
        firstEpoch.NRE.epochTransactionsRoot = "0x" + firstEpo.substring(962, 1026);
        firstEpoch.NRE.epochReceiptsRoot = "0x" + firstEpo.substring(1026, 1090);
        firstEpoch.NRE.submittedAt = bigInt(parseInt("0x" + firstEpo.substring(1090, 1154))).toString();
        firstEpoch.NRE.finalizedAt = bigInt(parseInt("0x" + firstEpo.substring(1154, 1218))).toString();
        firstEpoch.NRE.finalized = bigInt(parseInt("0x" + firstEpo.substring(1218, 1282))).toString();
        firstEpoch.NRE.challenging = bigInt(parseInt("0x" + firstEpo.substring(1282, 1346))).toString();
        firstEpoch.NRE.challenged = bigInt(parseInt("0x" + firstEpo.substring(1346, 1410))).toString();
        const totalStaked = bigInt(parseInt(totStaked)).toString();
        const selfStaked = bigInt(parseInt(selfStake)).toString();
        const userStaked = bigInt(parseInt(userStake)).toString();
       const isCommissionRateNegative = bigInt(parseInt(CommissionRateNegative)).toString();
       const commissionRate = bigInt(parseInt(commRate)).toString();
       const powerTONSeigRate = bigInt(parseInt(powerTONSeigRt)).toString();
       const daoSeigRate =  bigInt(parseInt(daoSeigRt)).toString();
       const relativeSeigRate =  bigInt(parseInt(relativeSeigRt)).toString();
       const delayedCommissionRateNegative = bigInt(parseInt(delayedCommissionRateNeg)).toString();
       const delayedCommissionRate = bigInt(parseInt(delayedCommissionRt)).toString();
       const delayedCommissionBlock = bigInt(parseInt(delayedCommBlock)).toString();
       const withdrawalDelay = bigInt(parseInt(withdrawDelay)).toString();
       const globalWithdrawalDelay =  bigInt(parseInt(globalWithdrawDelay)).toString();
       const minimumAmount =  bigInt(parseInt(minAmount)).toString();
      
       const deployedAt = firstEpoch.timestamp;
       const lastFinalizedEpochNumber = currentFork.lastFinalizedEpoch;
       const lastFinalizedBlockNumber = currentFork.lastFinalizedBlock;
       const finalizeCount = parseInt(lastFinalizedEpochNumber) + 1;
       const lastFinalizedAt = await getLastFinalizedAt(lastFinalizedEpochNumber, lastFinalizedBlockNumber);
       const lastFinalized = await getRecentCommit(operator, layer2);
      
       const tos = toBN(tonTotalSupply).mul(toBN('1000000000')).add(toBN(totTotalSupply)).sub(toBN(tonBalanceOfWTON));
       const fromBlockNum = await BlockchainModule.callMethod("lastCommitBlock", SeigManager,layer2);
       const fromBlockNumber = bigInt(parseInt(fromBlockNum)).toString();
      const totalStakedAmt = await BlockchainModule.callMethod("totalSupply", Tot, "");
      const totalStakedAmount =  bigInt(parseInt(totalStakedAmt)).toString();
      const pseigRt =  await BlockchainModule.callMethod("relativeSeigRate", SeigManager, "");
      const pseigRate = bigInt(parseInt(pseigRt)).toString();
      const seigniorage = calculateExpectedSeig(
        new BN(fromBlockNumber),
        new BN(blockNumber),
        new BN(userStaked),
        new BN(totalStakedAmount),
        new BN(tos),
        new BN(pseigRate)
         );
        const notWithdrawableRequests = filterNotWithdrawableRequests(pendingRequests);

          const withdrawableRequests = filterWithdrawableRequests(pendingRequests);

          const userNotWithdrawable = getUserNotWithdrawable(notWithdrawableRequests);
          const userWithdrawable = getUserWithdrawable(withdrawableRequests);
          operatorFromLayer2.address = operator;
          // operatorFromLayer2.lastFinalizedAt = lastFinalizedAt;
          operatorFromLayer2.lastFinalizedAt = (lastFinalized[0]==='0') ? lastFinalizedAt : lastFinalized[0];
          operatorFromLayer2.finalizeCount = lastFinalized[1];
          operatorFromLayer2.deployedAt = deployedAt;
          operatorFromLayer2.totalDeposit = _WTON(totalDeposit, WTON_UNIT);
          operatorFromLayer2.totalStaked = _WTON(totalStaked, WTON_UNIT);
          operatorFromLayer2.selfDeposit = _WTON(selfDeposit, WTON_UNIT);
          operatorFromLayer2.selfStaked = _WTON(selfStaked, WTON_UNIT);

          operatorFromLayer2.userDeposit = _WTON(userDeposit, WTON_UNIT);
          operatorFromLayer2.userStaked = _WTON(userStaked, WTON_UNIT);
          operatorFromLayer2.userSeigs = _WTON(seigniorage, WTON_UNIT);
          // operatorFromLayer2.userSeigs
          //   = operator.toLowerCase() === user.toLowerCase() ? seigs.operatorSeigs : _WTON(seigniorage, WTON_UNIT);
          operatorFromLayer2.isCommissionRateNegative = isCommissionRateNegative;
          operatorFromLayer2.commissionRate = _WTON(commissionRate, WTON_UNIT);

          operatorFromLayer2.delayedCommissionRateNegative = delayedCommissionRateNegative;
          operatorFromLayer2.delayedCommissionRate = _WTON(delayedCommissionRate, WTON_UNIT);
          operatorFromLayer2.delayedCommissionBlock = delayedCommissionBlock;
          operatorFromLayer2.powerTONSeigRate = _WTON(powerTONSeigRate, WTON_UNIT);
          operatorFromLayer2.daoSeigRate = _WTON(daoSeigRate, WTON_UNIT);
          operatorFromLayer2.relativeSeigRate = _WTON(relativeSeigRate, WTON_UNIT);
          operatorFromLayer2.withdrawalRequests = pendingRequests;
          operatorFromLayer2.notWithdrawableRequests = notWithdrawableRequests;
          operatorFromLayer2.withdrawableRequests = withdrawableRequests;
          // already wrapped with WTON
          operatorFromLayer2.userNotWithdrawable = userNotWithdrawable;
          operatorFromLayer2.userWithdrawable = userWithdrawable;
          operatorFromLayer2.userRedelegatable = userWithdrawable.add(userNotWithdrawable);
          operatorFromLayer2.userReward = userNotWithdrawable;
          operatorFromLayer2.withdrawalDelay = withdrawalDelay;
          operatorFromLayer2.globalWithdrawalDelay = globalWithdrawalDelay;
          operatorFromLayer2.minimumAmount = minimumAmount;
          return operatorFromLayer2;
        })
      );
      context.commit('SET_OPERATORS', operatorsFromLayer2);
    context.commit("SET_LOADING", true);
    },
    async setBalance (context) {
      const user = context.state.user;

      const TON = context.state.TON;
      const WTON = context.state.WTON;
      const PowerTON = context.state.PowerTON;
      
      const ethBalance = await  BlockchainModule.getBalance();
      const tonBal = await BlockchainModule.callMethod("balanceOf", TON, user);
      const tonBalance =  bigInt(parseInt(tonBal)).toString();
      
      const wtonBal = await  BlockchainModule.callMethod("balanceOf", WTON, user);
      const wtonBalance =  bigInt(parseInt(wtonBal)).toString();
      const powerBal = await BlockchainModule.callMethod("powerOf", PowerTON, user);
      const powerBalance =  bigInt(parseInt(powerBal)).toString();
     
      context.commit('SET_ETHBALACE', _ETH.wei(ethBalance.toString()));
      context.commit('SET_TONBALACE', _TON.wei(tonBalance.toString()));
      context.commit('SET_WTON_BALANCE', _WTON.ray(wtonBalance.toString()));
      context.commit('SET_POWER', _POWER.ray(powerBalance.toString()));
      
    },
    async setCurrentRound (context) {
      const user = context.state.user;
      const WTON = context.state.WTON;
      const PowerTON = context.state.PowerTON;
      const currentRoundIn = await BlockchainModule.callMethod("currentRound", PowerTON, "");
      const currentRoundIndex = bigInt(parseInt(currentRoundIn)).toString();
      const [
        round,
        bal,
        totDeposits,
        pow,
      ] = await Promise.all([
        BlockchainModule.callMethod("rounds", PowerTON, currentRoundIndex),
        BlockchainModule.callMethod("balanceOf", WTON, PowerTON),
        BlockchainModule.callMethod("totalDeposits", PowerTON, ""),
        BlockchainModule.callMethod("powerOf", PowerTON, user)
      ]);
      currentRound = {},
      currentRound.startTime = bigInt(parseInt("0x" + round.substring(2, 66))).toString();
      currentRound.endTime = parseInt("0x" + round.substring(66,130));
     currentRound.winner = "0x" + round.substring(218,258)
     const balance = bigInt(parseInt(bal)).toString();
     const totalDeposits = bigInt(parseInt(totDeposits)).toString();
     const power = bigInt(parseInt(pow)).toString();
     const userPower = _POWER.ray(power);
     const totalPower = _POWER.ray(totalDeposits);
     const reward = new BN(balance);
     currentRound.index = currentRoundIndex;
     currentRound.reward = _WTON.ray(reward);
     if (!totalPower.eq(_POWER.ray('0'))) {
      const winningProbability = userPower.div(totalPower);
      currentRound.winningProbability = `${numeral(winningProbability.toNumber()).format('0.00%')}`;
    } else {
      currentRound.winningProbability = '0.00%';
    }
    context.commit('SET_CURRENT_ROUND', currentRound);
     
    },
    async setHistory (context) {
      const user = context.state.user;
      const userHistory = await getHistory(user);

      context.commit('SET_USER_HISTORY', userHistory.map(h => h.history));
    },
    async setRounds (context) {
      const PowerTON = context.state.PowerTON;
      const roundEndEvent = web3EthABI.encodeEventSignature('RoundEnd(uint256,address,uint256)');
      const events = await BlockchainModule.getPastEvents(PowerTON, roundEndEvent );

      const rounds = events.map(async (event) => {
        const blockNumber = parseInt(event.blockNumber);
        const blockTimestamp = await BlockchainModule.getTimeStamp(blockNumber);
        const ReturnValues = event.data;
        const round = bigInt(parseInt("0x" + ReturnValues.substring(2, 66))).toString();
        const winner = toChecksumAddress("0x" + ReturnValues.substring(90, 130));
        const  reward = bigInt(parseInt("0x" + ReturnValues.substring(130, 194))).toString();
        return {
          index: parseInt(round),
          winner: winner,
          reward: _WTON.ray(reward),
          timestamp: blockTimestamp
        };
        // const returnValues = event.returnValues;
      });
      context.commit('SET_ROUNDS', await Promise.all(rounds));
    },
    setOpenOperator (context, operator) {
      context.commit('SET_SELECTED_OPERATOR', operator)
    }
  },

  getters: {initialState: (state) => {
    return isEqual(state, initialState);
  },
  operatorsStaked: state => {
    if (state.operators && state.operators.length > 0) {
      return state.operators.filter(operator => parseInt(operator.userStaked) > 0);
    }
    else return [];
  },
  operatorByLayer2: (state) => (layer2) => {
    return cloneDeep(state.operators.find(operator => operator.layer2.toLowerCase() === layer2.toLowerCase()));
  },
  userTotalDeposit: (state) => {
    const initialAmount = _WTON.ray('0');
    const reducer = (amount, operator) => amount.add(operator.userDeposit);

    return state.operators.reduce(reducer, initialAmount);
  },
  userTotalStaked: (state) => {
    const initialAmount = _WTON.ray('0');
    const reducer = (amount, operator) => amount.add(operator.userStaked);

    return state.operators.reduce(reducer, initialAmount);
  },
  userTotalSeigs: (state) => {
    const initialAmount = _WTON.ray('0');
    const reducer = (amount, operator) => amount.add(operator.userSeigs);

    return state.operators.reduce(reducer, initialAmount);
  },
  userTotalNotWithdrawable: (state) => {
    const initialAmount = _WTON.ray('0');
    const reducer = (amount, operator) => amount.add(operator.userNotWithdrawable);

    return state.operators.reduce(reducer, initialAmount);
  },
  userTotalWithdrawable: (state) => {
    const initialAmount = _WTON.ray('0');
    const reducer = (amount, operator) => amount.add(operator.userWithdrawable);

    return state.operators.reduce(reducer, initialAmount);
  },
  userTotalReward: (_, getters) => {
    return getters.userTotalStaked
      .add(getters.userTotalWithdrawable)
      .add(getters.userTotalNotWithdrawable);
    // .sub(getters.userTotalDeposit);
  },
},
});
