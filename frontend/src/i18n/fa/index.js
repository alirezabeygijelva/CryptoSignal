// This is just an example,
// so you can safely delete all default props below

export default {
  header: {
    home: 'خانه',
    signal: 'سیگنال',
    admin: 'ادمین',
    setting: 'تنظیمات',
    logout: 'خروج',
    login: 'ورود',
    signup: 'ثبت نام'
  },
  footer: {
    description: 'توضحات شرکت را اینجا وارد کنید.',
    socials: 'شبکه های اجتماعی',
    contactUs: 'تماس با ما',
    supportPhone: 'تلفن پشتیبانی: 123-456-789',
    linkedin: "لینکدین",
    instagram: 'اینستاگرام',
    telegram: 'تلگرام'
  },
  home: {
    assetsTable: {
      title: 'قیمت لحظه‌ای',
      header: {
        logo: 'لوگو',
        name: 'نام',
        price: 'آخرین قیمت',
        percent: 'تغییرات روزانه',
        change: 'تغییرات قیمت روزانه',
        volume: 'حجم معاملات',
        bid: 'بهترین قیمت پیشنهادی',
        ask: 'بهترین قیمت درخواستی'
      }
    }
  },
  signal: {
    title: 'سیگنال‌های اشتراک',
    numberOfSignals: 'تعداد سیگنال ها برای این ارز',
    editTooltip: 'ویرایش',
    newSignal: 'سیگنال جدید',
    updateSignal: 'ویرایش سیگنال',
    market: 'مارکت',
    symbol: 'نام ارز',
    targetType: 'نوع تارگت',
    targetValue: 'مقدار تارگت',
    message: 'متن پیام',
    notitficationType: 'ارسال اعلان از طریق',
    timeout: 'مدت زمان وقفه از آخرین ارسال',
    lastTriggered: 'آخرین اعلان ارسالی',
    enabled: 'وضعیت ارسال',
    createSignalSuccess: 'سیگنال با موفقیت اضافه و فعال شد',
    deleteSignalSuccess: 'سیگنال با موفقیت حذف شد',
    updateSignalSuccess: 'سیگنال با موفقیت بروزرسانی شد',
    missingAnySignal: 'در حال حاضر شما هیچ سیگنالی ندارید. لطفا در صورت نیاز از طریق دکمه بالا یک سیگنال جدید ایجاد کنید.'
  },
  admin: {
    binanceStream: 'بایننس استریم',
    markets: 'مارکت ها',
    newMarket: 'مارکت جدید',
    marketName: 'نام مارکت',
    marketDescription: 'توضیحات مارکت',
    marketType: 'نوع مارکت',
    operation: 'عملیات',
    addBinanceStreamSuccess: 'بایننس استریم با موفقیت اضافه شد',
    deleteBinanceStreamSuccess: 'بایننس استریم با موفقیت حذف شد',
    addMarketSuccess: 'مارکت با موفقیت ایجاد شد',
    deleteMarketSuccess: 'مارکت با موفقیت حذف شد'
  },
  general: {
    cancel: 'لغو',
    save: 'ذخیره',
    edit: 'ویرایش',
    operation: 'عملیات',
    close: 'بستن',
    delete: 'حذف',
    deleteDialog: 'آیا مطمئن هستید که می خواهید حذف کنید؟'
  },
  settings: {
    editProfile: 'ویرایش پروفایل',
    editPassword: 'ویرایش رمز عبور',
    firstName: 'نام',
    lastName: 'نام خانوادگی',
    email: 'ایمیل',
    phone: 'شماره تلفن',
    telegramId: 'آیدی چت تلگرام',
    password: 'رمز عبور فعلی',
    newPassword: 'رمز عبور جدید',
    confirmNewPassword: 'تکرار رمز عبور جدید',
  },
  login: {
    title: 'ورود با ایمیل',
    username: 'نام کاربری (ایمیل)',
    password: 'رمز عبور',
    submit: 'ورود',
    missingAccount: 'حساب کاربری ندارید؟',
    signup: 'ایجاد حساب کاربری جدید',
    forgetPassword: 'رمزعبورتان را فراموش کردید؟',
    resetPassword: 'بازنشانی رمز عبور'
  },
  signup: {
    title: 'ایجاد حساب کاربری',
    firstName: 'نام',
    lastName: 'نام خانوادگی',
    email: 'ایمیل',
    phone: 'شماره تلفن',
    password: 'رمز عبور',
    confirmPassword: 'تکرار رمز عبور',
    submit: 'ثبت نام',
    haveAccount: 'آیا از قبل حساب کاربری دارید؟',
    login: 'ورود با ایمیل'
  },
  forgetPassword: {
    title: 'فراموشی رمز عبور',
    email: 'ایمیل',
    emailHint: 'لطفا ایمیل مرتبط با حساب کاربری خود را وارد کنید',
    submit: 'بررسی'
  },
  resetPassword: {
    title: 'بازنشانی رمز عبور',
    submit: 'تغییر رمز عبور',
    password: 'رمز عبور جدید',
    confirmPassword: 'تکرار رمز عبور جدید',
  },
  notifications: {
    failed: 'اقدام ناموفق بود',
    success: 'اقدام موفقیت آمیز بود',
    successfulLogin: 'با موفقیت لاگین شدید',
    successfulSignup: 'با موفقیت ثبت نام شدید',
    succesfulUpdate: 'حساب شما با موفقیت آپدیت شد',
    successfulRequestResetPassword: 'یک ایمیل حاوی لینک بازنشانی رمز عبور برای شما ارسال شد',
    successfulResetPassword: 'پسورد با موفقیت تغییر کرد'
  },
  errors: {
    SORT_FIELD_NOT_SUPPORTED: 'فیلد انتخاب شده برای مرتب سازی غیرمجاز است',
    INVALID_MARKET_TYPE: 'نوع مارکت غیرمجاز است',
    MARKET_EXISTS: 'مارکتی از قبل با این مشخصات وجود دارد',
    MARKET_NOT_FOUND: 'مارکت مورد نظر وجود ندارد',
    BINANCE_STREAM_EXISTS: 'استریم بایننس از قبل وجود دارد',
    BINANCE_STREAM_NOT_FOUND: 'استریم بایننس با این مشخصات یافت نشد',
    ASSETS_NOT_FOUND: 'هیچ دارایی با این مشخصات یافت نشد',
    SYMBOL_NOT_MATCH: 'هیچ ارزی با این مشخصات یافت نشد',
    INVALID_TARGET_TYPE: 'نوع تارگت مشخص شده نامعتبر است',
    SUBSCRIPTION_EXISTS: 'اشتراکی از قبل با این مشخصات وجود دارد',
    SUBSCRIPTION_NOT_FOUND: 'اشتراکی با این مشخصات یافت نشد',
    USER_NOT_FOUND: 'کاربری با این مشخصات یافت نشد',
    INVALID_USERNAME: 'کاربری با این مشخصات یافت نشد',
    UNAVAILABLE_EMAIL: 'ایمیل مورد نظر دردسترس نیست یا توسط فرد دیگری انتخاب شده است',
    UNAVAILABLE_PHONE: 'شماره تلفن مورد نظر در دسترس نیست یا توسط فرد دیگری انتخاب شده است',
    INVALID_USERNAME_OR_PASSWORD: 'نام کاربری یا رمز عبور اشتباه وارد شده است',
    INVALID_REFRESH_TOKEN: 'رفرش توکن نامعتبر است',
    PASSWORD_NOT_MATCH: 'رمز عبور و تکرار آن باهم مطابقت ندارد',
    INVALID_ARGUMENT: 'ورودی های نامعتبر است. لطفا مطمئن شوید ورودی های درستی وارد کردید',
    EMPTY_FILEDS: 'برخی فیلدها مقداردهی نشده است. لطفا همه فیلدها را پر کنید',
    INVALID_RESET_PASSWORD_TOKEN: 'لینک بازنشانی پسورد نامعتبر است یا منقضی شده است',
    INVALID_PASSWORD: 'رمز عبور اشتباه است'
  },
  formHints: {
    firstName: 'نام باید حداقل ۳ کاراکتر داشته باشد',
    lastName: 'نام خانوادگی باید حداقل ۱ کاراکتر داشته باشد',
    username: 'ایمیل باید در قالب example[at]mail.com باشد',
    password: 'رمز عبور باید حداقل ۳ کاراکتری باشد',
    confirmPassword: 'تکرار رمز عبور باید دقیقا با رمز عبور یکسان باشد',
    phone: 'شماره تلفن همراه را بدون صفر اول وارد کنید و یا با کد کشور وارد کنید برای مثال 989123456789',
    telegram: 'لطفا برای فهمیدن آیدی تلگرام از طریق بات تلگرام @telegram-bot-username پیام /start را ارسال کنید تا بات ما تلگرام آیدی را برای شما ارسال کند.',
    signal: {
      maxPrice: 'بیشترین قیمت: زمانی ارسال میشود که قیمت از مقدار تارگت بیشتر باشد',
      minPrice: 'کمترین قیمت: زمانی ارسال میشود که قیمت از مقدار تارگت کمتر باشد',
      price: 'قیمت: زمانی ارسال میشود که قیمت دقیقا برابر با مقدار تارگت باشد',
      maxChange: 'بیشترین تغییرات: زمانی ارسال میشود که تغییرات قیمت از مقدار تارگت بیشتر باشد',
      minChange: 'کمترین تغییرات: زمانی ارسال میشود که تغییرات قیمت از مقدار تارگت کمتر باشد',
      change: 'تغییرات: زمانی ارسال میشود که تغییرات قیمت دقیقا برابر با مقدار تارگت باشد',
      volume: 'حجم: زمانی ارسال میشود که حجم معاملات بزرگتر یا مساوی با مقدار تارگت باشد',
      timeout: {
        zero: 'بدون توقف',
        oneMin: '۱ دقیقه',
        fiveMin:'۵ دقیقه',
        tenMin: '۱۰ دقیقه',
        fifteenMin: '۱۵ دقیقه',
        thirtyMin: '۳۰ دقیقه',
        oneHour: '۱ ساعت',
        fourHour: '۴ ساعت',
        eightHour: '۸ ساعت',
        twelveHour: '۱۲ ساعت',
        oneDay: '۱ روز'
      },
      message: 'متن پیام میتواند شامل دو %s باشد که %s اولی مقدار واقعی بر اساس نوع تارگت و دومین، مقدار تارگت تنظیم شده را برمیگرداند.',
      sendType: 'اگر تلگرام را انتخاب کردید از قسمت تنظیمات، تلگرام Id را برای حسابتان تنظیم کنید.',
    }
  }
}
