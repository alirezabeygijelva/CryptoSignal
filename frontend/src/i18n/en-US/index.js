// This is just an example,
// so you can safely delete all default props below

export default {
  header: {
    home: 'Home',
    signal: 'Signals',
    admin: 'Admin',
    setting: 'Setting',
    logout: 'Logout',
    login: 'Login',
    signup: 'Signup',
    select_lang: 'Select Language'
  },
  footer: {
    description: 'Your company description here.',
    socials: 'Social Networks',
    contactUs: 'Contact Us',
    supportPhone: 'Phone: +1-123-456-789',
    linkedin: "Linkedin",
    instagram: 'Instagram',
    telegram: 'Telegram'
  },
  home: {
    assetsTable: {
      title: 'Current Price',
      header: {
        logo: 'Logo',
        name: 'Name',
        price: 'Last Price',
        percent: 'Daily Change',
        change: 'Daily Price Change',
        volume: 'Volume',
        bid: 'Best Bid Price',
        ask: 'Best Ask Price'
      }
    }
  },
  signal: {
    title: 'Subscription Signals',
    numberOfSignals: 'Number of signals for this currency',
    editTooltip: 'Edit',
    newSignal: 'New Signal',
    updateSignal: 'Update Signal',
    market: 'Market',
    symbol: 'Symbol',
    targetType: 'Target Type',
    targetValue: 'Target Value',
    message: 'Message',
    notitficationType: 'Send notification via',
    timeout: 'Timeout',
    lastTriggered: 'Last Triggered',
    enabled: 'Send Status',
    createSignalSuccess: 'The signal has been successfully added and activated',
    deleteSignalSuccess: 'The signal was successfully removed',
    updateSignalSuccess: 'The signal has been updated successfully',
    missingAnySignal: 'You currently have no signal. Please create a new signal via the button above.'
  },
  admin: {
    binanceStream: 'Binance Stream',
    markets: 'Markets',
    newMarket: 'Add Market',
    marketName: 'Market Name',
    marketDescription: 'Description of market',
    marketType: 'Market Type',
    operation: 'Operation',
    addBinanceStreamSuccess: 'Binance Stream has been successfully added',
    deleteBinanceStreamSuccess: 'Binance Stream has been successfully removed',
    addMarketSuccess: 'Market created successfully',
    deleteMarketSuccess: 'The market was successfully removed'
  },
  general: {
    cancel: 'Cancel',
    save: 'Save',
    edit: 'Edit',
    operation: 'Operation',
    close: 'Close',
    delete: 'Delete',
    deleteDialog: 'Are you sure you want to delete?'
  },
  settings: {
    editProfile: 'Edit Profile',
    editPassword: 'Edit Password',
    firstName: 'First Name',
    lastName: 'Last Name',
    email: 'Email',
    phone: 'Phone Number',
    telegramId: 'Telegram Chat Id',
    password: 'Current Password',
    newPassword: 'New Passowrd',
    confirmNewPassword: 'New Repeat Password',
  },
  login: {
    title: 'Login Via Email',
    username: 'Username (Email)',
    password: 'Password',
    submit: 'Login',
    missingAccount: 'Don\'t have an account?',
    signup: 'Create new account',
    forgetPassword: 'Forgot your password?',
    resetPassword: 'Reset Password'
  },
  signup: {
    title: 'Register Account',
    firstName: 'First Name',
    lastName: 'Last Name',
    email: 'Email',
    phone: 'Phone Number',
    password: 'Passowrd',
    confirmPassword: 'Repeat Password',
    submit: 'Register',
    haveAccount: 'Do you already have an account?',
    login: 'Login via email'
  },
  forgetPassword: {
    title: 'Forget Password',
    email: 'email',
    emailHint: 'Please enter the email associated with your account',
    submit: 'Check'
  },
  resetPassword: {
    title: 'Reset Password',
    submit: 'Change Password',
    password: 'New Passowrd',
    confirmPassword: 'New Repeat Password',
  },
  notifications: {
    failed: 'Action failed',
    success: 'Action was successful',
    successfulLogin: 'You have successfully logged in',
    successfulSignup: 'You have successfully signup',
    succesfulUpdate: 'Your account has been successfully updated',
    successfulRequestResetPassword: 'An email containing a password reset link has been sent to you',
    successfulResetPassword: 'Password changed successfully'
  },
  errors: {
    SORT_FIELD_NOT_SUPPORTED: 'The field selected for sorting is not allowed',
    INVALID_MARKET_TYPE: 'The market type is not allowed',
    MARKET_EXISTS: 'There is already a market with this specification',
    MARKET_NOT_FOUND: 'The desired market does not exist',
    BINANCE_STREAM_EXISTS: 'Binance stream already exists',
    BINANCE_STREAM_NOT_FOUND: 'Binance stream was not found with this specification',
    ASSETS_NOT_FOUND: 'No assets were found with this specification',
    SYMBOL_NOT_MATCH: 'No currency was found with this specification',
    INVALID_TARGET_TYPE: 'The specified target type is invalid',
    SUBSCRIPTION_EXISTS: 'There is already a subscription with this specification',
    SUBSCRIPTION_NOT_FOUND: 'No subscription was found with this specification',
    USER_NOT_FOUND: 'User with this specification was not found',
    INVALID_USERNAME: 'User with this specification was not found',
    UNAVAILABLE_EMAIL: 'The desired email is not available or has been selected by someone else',
    UNAVAILABLE_PHONE: 'The desired phone number is not available or has been selected by someone else',
    INVALID_USERNAME_OR_PASSWORD: 'The username or password was entered incorrectly',
    INVALID_REFRESH_TOKEN: 'Refresh token is invalid',
    PASSWORD_NOT_MATCH: 'The password and its repetition do not match',
    INVALID_ARGUMENT: 'Invalid entries. Please make sure you entered the correct entries',
    EMPTY_FILEDS: 'Some fields are not set. Please fill in all fields',
    INVALID_RESET_PASSWORD_TOKEN: 'The password reset link is invalid or has expired',
    INVALID_PASSWORD: 'The password is incorrect'
  },
  formHints: {
    firstName: 'The first name must have at least 3 characters',
    lastName: 'The last name must have at least 1 characters',
    username: 'The email should be in the example[at]mail.com format',
    password: 'The password must be at least 3 characters long',
    confirmPassword: 'Repeating the password must be exactly the same as the password',
    phone: 'Enter the mobile phone number without the first zero or enter it with the country code, for example +14155552671',
    telegram: 'Please send the message /start to find out the Telegram ID through Telegram bot @telegram-bot-username so that our bot will send you the Telegram ID.',
    signal: {
      maxPrice: 'Highest price: sent when the price is higher than the target value',
      minPrice: 'Lowest price: sent when the price is lower than the target value',
      price: 'Price: sent when the price is exactly equal to the target amount',
      maxChange: 'Highest changes: sent when the changes are greater than the target value',
      minChange: 'Lowest changes: sent when the price changes are less than the target value',
      change: 'Changes: sent when the price changes are exactly equal to the target amount',
      volume: 'Volume: sent when the transaction volume is greater than or equal to the target value',
      timeout: {
        zero: 'Without Timout',
        oneMin: '1 Minute',
        fiveMin:'5 Minutes',
        tenMin: '10 Minutes',
        fifteenMin: '15 Minutes',
        thirtyMin: '30 Minutes',
        oneHour: '1 Hour',
        fourHour: '4 Hours',
        eightHour: '8 Hours',
        twelveHour: '12 Hours',
        oneDay: '1 Day'
      },
      message: 'The text of the message can contain two %s, the first %s returns the actual value based on the type of target and the second returns the set target value.',
      sendType: 'If you choose Telegram, set the Telegram Id for your account from the settings section.',
    }
  }
}
