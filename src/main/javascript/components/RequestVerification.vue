<template>
<div class="dommo-request-form">
  <form @submit="submit">
    <input type="hidden" v-model="app_id" value="__TOKEN__" />
    <input type="hidden" name="token" value="__TOKEN__" />
    <div class="field-group">
      <label for="name">Full Name</label>
      <input type="text" v-model="requestDto.customerName" class="control" />
    </div>
    <div class="field-group">
      <label>Phone Number</label>
      <input type="text" v-model="requestDto.phone" placeholder="0888123456" class="control" />
    </div>
    <div class="field-group">
      <label>Service</label>
      <select v-model="requestDto.serviceNameID">
        <option :key="service" v-for="service in services">{{service}}</option>
      </select>
    </div>
    <div class="field-group">
      <label>Transaction ID / Ref</label>
      <input type="text" v-model="requestDto.transactionReference" class="control" />
    </div>
    <div class="field-group">
      <label>Other Transaction Details</label>
      <input type="text" v-model="requestDto.transactionDetail" class="control" />
    </div>
    <button class="button is-success">Submit</button>
  </form>
</div>
</template>
<script>
import axios from 'axios';

function apiRoute(path) {
    if ('_apiBaseUri' in window) {
        return window._apiBaseUri + path;
    }
    return path;
}

export default {
    props: [
        'productID', 'customerID', 'amount',
        'failCallbackUrl', 'verifiedCallbackUrl'
    ],

    mounted() {
        // TODO: axios.get(window._apiBaseUri + "/form-token")
    },

    data() {
        return {
            requestDto: {
                customerName: "",
                phone: "",
                amount: 0.0,
                transactionReference: "",
                transactionDetail: "",
                serviceNameID: "",
                productID: "",
                customerID: "",
                txnID: "",
                failCallbackUrl: "",
                verifiedCallbackUrl: ""
            },
            services: [
                "Test Service", "TNM Mpamba",
                "Airtel Money", "NBM Mo626",
                "NBS 322", "FDH 522"
            ]
        }
    },

    methods: {
        submit(event) {
            event.preventDefault();
            axios.post(apiRoute("/request-verification"), this.requestDto)
                .then(response => {
                    alert('Requested verification successfully. Please be patient')
                    console.log(response.data);
                })
                .catch(err => {
                    alert('Failed to requested verification. Please, try again.')
                    console.log("Got error", err);
                });

            return false;
        }
    }
}
</script>