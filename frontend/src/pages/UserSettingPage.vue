<template>
  <q-page class="row justify-center q-py-lg">
    <div class="q-pa-md col-lg-8 col-md-8 col-sm-10 col-xs-12">
      <q-card>
        <q-card-section class="bg-blue-grey-9 text-white">
          <div class="text-h6">{{ $t('settings.editProfile') }}</div>
        </q-card-section>

        <q-card-section class="items-start q-px-lg">
          <q-input v-model="firstNameModel" :label="$t('settings.firstName')"/>
          <q-input v-model="lastNameModel" :label="$t('settings.lastName')"/>
          <q-input v-model="emailModel" :label="$t('settings.email')"/>
          <q-input v-model="phoneModel" :label="$t('settings.phone')" :hint="$t('formHints.phone')"/>
          <q-input v-model="telegramIdModel" :label="$t('settings.telegramId')" :hint="$t('formHints.telegram')" />
        </q-card-section>
        <q-card-actions class="q-px-lg q-pb-lg">
          <q-btn color="positive" :label="$t('general.save')" @click="onProfileUpdate"/>
        </q-card-actions>
      </q-card>

      <q-card class="my-card q-mt-lg">
        <q-card-section class="bg-blue-grey-9 text-white">
          <div class="text-h6">{{ $t('settings.editPassword') }}</div>
        </q-card-section>

        <q-card-section class="items-start q-px-lg">
          <q-input v-model="password" type="password" :label="$t('settings.password')" />
          <q-input v-model="newPassword" type="password" :label="$t('settings.newPassword')" :hint="$t('formHints.password')" />
          <q-input v-model="confirmNewPassword" type="password" :label="$t('settings.confirmNewPassword')" :hint="$t('formHints.confirmPassword')" />
        </q-card-section>
        <q-card-actions class="q-px-lg q-pb-lg">
          <q-btn color="positive" :label="$t('general.save')" @click="onChangePassword"/>
        </q-card-actions>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import {mapGetters, mapMutations} from "vuex";

export default {
  name: "UserSettingPage",

  data() {
    return {
      firstNameModel: '',
      lastNameModel: '',
      emailModel: '',
      phoneModel: '',
      telegramIdModel: '',
      password: undefined,
      newPassword: undefined,
      confirmNewPassword: undefined
    }
  },

  methods: {
    onProfileUpdate() {
      this.$q.loading.show()
      this.$api.updateUserAccount(this.firstNameModel, this.lastNameModel, this.emailModel, this.phoneModel, this.telegramIdModel, this.$cookies.get('access_token'))
        .then((response) => {
          this.$q.notify({
            progress: true,
            message: this.$t('notifications.succesfulUpdate'),
            type: 'positive'
          })
          this.setUserAccount(response.data.data)
        })
        .catch((error) => {
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },
    onChangePassword() {
      this.$q.loading.show()
      this.$api.changePassword(this.password, this.newPassword, this.confirmNewPassword, this.$cookies.get('access_token'))
        .then((response) => {
          this.$q.notify({
            progress: true,
            message: this.$t('notifications.successfulResetPassword'),
            type: 'positive'
          })
        })
        .catch((error) => {
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },
    ...mapMutations(['setUserAccount'])
  },

  computed: {
    ...mapGetters(["firstName", "lastName", "email", "phone", "telegramId"])
  },

  beforeMount() {
    this.firstNameModel = this.firstName
    this.lastNameModel = this.lastName
    this.emailModel = this.email
    this.phoneModel = this.phone
    this.telegramIdModel = this.telegramId
  }
}
</script>

<style scoped>

</style>
