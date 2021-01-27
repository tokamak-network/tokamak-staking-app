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
            context.dispatch("setEthBalance");
          });
        }
      });
    },
    async setEthBalance(context) {
      BlockchainModule.getBalance((result) => {
        context.commit("SET_ETHBALACE", _ETH.wei(result.toString()));
        context.dispatch("set");
      });
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
      await context.dispatch("setManagers", managers);
      await context.dispatch("setOperatorsWithRegistry", operators);
      await Promise.all([
        context.dispatch("setOperators", blockNumber),
        // context.dispatch('setBalance'),
        // context.dispatch('setCurrentRound'),
        // context.dispatch('setRounds'),
        // context.dispatch('setHistory'),
        // context.dispatch('setUncommittedCurrentRoundReward', blockNumber),
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

      const operators = context.state.operators;
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
          const getLastFinalizedAt = async () => {
            const epo = await BlockchainModule.callSmartMethod(
              "getEpoch",
              layer2,
              currentForkNumber,
              "0"
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
              "0"
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
          let requestIndex = await BlockchainModule.callSmartMethod('withdrawalRequestIndex', DepositManager, layer2, user );
          const index = bigInt(parseInt(requestIndex)).toString();
          const pendingRequests = [];
          for (const _ of range(numPendingRequests)) {
            const req = await BlockchainModule.callSmartFunc('withdrawalRequest', DepositManager, layer2, user, index )
            const request = {}; 
            request.withdrawableBlockNumber =  bigInt(parseInt("0x" + req.substring(2, 66))).toString();
            request.amount = bigInt(parseInt("0x" + req.substring(66, 130))).toString();
            request.processed = bigInt(parseInt("0x" + req.substring(130, 194))).toString();
            pendingRequests.push(request);
            requestIndex++;
          }
          return Promise.all(pendingRequests);
         }
         const filterNotWithdrawableRequests = (requests) => {
          return requests.filter(request => parseInt(request.withdrawableBlockNumber) > blockNumber);
        };
        const filterWithdrawableRequests = (requests) => {
          return requests.filter(request => parseInt(request.withdrawableBlockNumber) <= blockNumber);
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
        let [
          currentfork,
          firstEpoch,
          // totalDeposit,
          // selfDeposit,
          // userDeposit,
          // totalStaked,
          // selfStaked,
          // userStaked,
          // pendingRequests,
          // seigs, // operatorSeigs, userSeigs, layer2Seigs
          // isCommissionRateNegative,
          // commissionRate,
          // powerTONSeigRate,
          // daoSeigRate,
          // relativeSeigRate,
          // delayedCommissionRateNegative,
          // delayedCommissionRate,
          // delayedCommissionBlock,
          // withdrawalDelay,
          // globalWithdrawalDelay,
          // minimumAmount,
        ] = await Promise.all([
          BlockchainModule.callMethod("forks", layer2, currentForkNumber.toString()),
          BlockchainModule.callSmartMethod(
            "getEpoch",
            layer2,
            "0",
            "0"
          )
          // getDeposit(),
          // getDeposit(operator),
          // getDeposit(user),
          // Coinage.methods.totalSupply().call(),
          // Coinage.methods.balanceOf(operator).call(),
          // Coinage.methods.balanceOf(user).call(null, blockNumber),
          // getPendingRequests(),
          // getExpectedSeigs(),
          // SeigManager.methods.isCommissionRateNegative(layer2).call(),
          // SeigManager.methods.commissionRates(layer2).call(),
          // SeigManager.methods.powerTONSeigRate().call(),
          // SeigManager.methods.daoSeigRate().call(),
          // SeigManager.methods.relativeSeigRate().call(),
          // SeigManager.methods.delayedCommissionRateNegative(layer2).call(),
          // SeigManager.methods.delayedCommissionRate(layer2).call(),
          // SeigManager.methods.delayedCommissionBlock(layer2).call(),
          // DepositManager.methods.withdrawalDelay(layer2).call(),
          // DepositManager.methods.globalWithdrawalDelay().call(),
          // SeigManager.methods.minimumAmount().call(),
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

        console.log(firstEpoch);
        })
      );

      context.commit("SET_LOADING", true);
    },
  },

  getters: {},
});
