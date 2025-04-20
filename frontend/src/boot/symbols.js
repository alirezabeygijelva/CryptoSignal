import {boot} from "quasar/wrappers";
import btc from '../assets/icons/currencies/bitcoin.png'
import eth from '../assets/icons/currencies/ethereum.png'
import sol from '../assets/icons/currencies/solana.png'
import bnb from '../assets/icons/currencies/binance.png'
import xrp from '../assets/icons/currencies/ripple.png'
import usdc from '../assets/icons/currencies/usdc.png'
import doge from '../assets/icons/currencies/dogecoin.png'
import ada from '../assets/icons/currencies/cardano.png'
import ton from '../assets/icons/currencies/ton.png'
import trx from '../assets/icons/currencies/trx.png'
import avax from '../assets/icons/currencies/avalanche.png'
import shib from '../assets/icons/currencies/shiba-inu.png'
import bch from '../assets/icons/currencies/bitcoin-cash.png'
import dot from '../assets/icons/currencies/polkadot.png'
import dai from '../assets/icons/currencies/dai.png'
import ltc from '../assets/icons/currencies/litecoin.png'
import uni from '../assets/icons/currencies/uniswap.png'
import kas from '../assets/icons/currencies/kaspa.png'
import xmr from '../assets/icons/currencies/monero.png'
import okb from '../assets/icons/currencies/okb.png'
import atom from '../assets/icons/currencies/cosmos.png'
import fil from '../assets/icons/currencies/filecoin.png'
import inj from '../assets/icons/currencies/injective.png'
import ftm from '../assets/icons/currencies/fantom.png'
import op from '../assets/icons/currencies/optimism.png'
import matic from '../assets/icons/currencies/polygon.png'

const symbols = {
  btc,
  BTCUSDT: btc,
  eth,
  ETHUSDT: eth,
  sol,
  SOLUSDT: sol,
  bnb,
  BNBUSDT: bnb,
  xrp,
  XRPUSDT: xrp,
  doge,
  DOGEUSDT: doge,
  usdc,
  USDCUSDT: usdc,
  ada,
  ADAUSDT: ada,
  ton,
  TONUSDT: ton,
  trx,
  TRXUSDT: trx,
  avax,
  AVAXUSDT: avax,
  shib,
  SHIBUSDT: shib,
  bch,
  BCHUSDT: bch,
  dot,
  DOTUSDT: dot,
  dai,
  DAIUSDT: dai,
  ltc,
  LTCUSDT: ltc,
  uni,
  UNIUSDT: uni,
  kas,
  KASUSDT: kas,
  xmr,
  XMRUSDT: xmr,
  okb,
  OKXUSDT: okb,
  atom,
  ATOMUSDT: atom,
  fil,
  FILUSDT: fil,
  inj,
  INJUSDT: inj,
  ftm,
  FTMUSDT: ftm,
  op,
  OPUSDT: op,
  matic,
  MATICUSDT: matic,
}

export default boot(({app}) => {
  // Set symbols icon
  app.config.globalProperties.$symbols = symbols
})
