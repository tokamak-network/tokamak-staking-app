import axios from 'axios';
import { getConfig } from '../../config.js';

function createInstance () {
  return axios.create({
    baseURL: getConfig().baseURL,
  });
}

function createInstatnceCandidate () {
  return axios.create({
    baseURL: getConfig().candidate,
  });
}

const instance = createInstance();
const candidate = createInstatnceCandidate();

export async function getCandidateCreateEvent () {
  const res = await candidate.get('/events', {
    params: {
      eventNames: 'CandidateContractCreated,Layer2Registered',
    },
  });
  if (res.data === '') return [];
  else return res.data.datas;
}

export async function getCandidates () {
  const res = await candidate.get('/layer2s/operators');
  if (res.data === '') return [];
  else return res.data.datas;
}

export async function getManagers () {
  const res = await instance.get('/managers');
  if (res.data === '') return [];
  else return res.data;
}

export async function getOperators () {
  const res = await instance.get('/operators');
  if (res.data === '') return [];
  else return res.data;
}

export async function updateOperator (layer2, formData) {
  const res = await instance.patch(
    '/operators',
    formData,
    {
      params: {
        layer2: layer2,
      },
    });
  return res.data;
}

export async function getHistory (user) {
  const res = await instance.get('/history', {
    params: {
      account: user,
    },
  });
  return res.data;
}

export async function addHistory (user, history) {
  await instance.post(
    '/history',
    { history },
    {
      params: {
        account: user,
      },
    });
}

export async function getTransactions (user) {
  const res = await instance.get('/transactions', {
    params: {
      from: user,
    },
  });
  if (res.data === '') return [];
  else return res.data;
}

export async function addTransaction (transaction) {
  const res = await instance.post(
    '/transactions',
    {
      type: transaction.type,
      amount: transaction.amount,
      transactionHash: transaction.transactionHash,
      target: transaction.target,
      receipt: transaction.receipt,
    },
    {
      params: {
        from: transaction.from,
      },
    });
  return res.data;
}
