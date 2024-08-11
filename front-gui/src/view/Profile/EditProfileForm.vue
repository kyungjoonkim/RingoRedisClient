<template>
  <card>
    <h5 slot="header" class="title">Edit Profile</h5>
    <div class="row">
<!--      <div class="col-md-5 pr-md-1">-->
<!--        <base-input-->
<!--            label="Company (disabled)"-->
<!--            placeholder="Company"-->
<!--            v-model="model.company"-->
<!--            disabled-->
<!--        >-->
<!--        </base-input>-->
<!--      </div>-->
      <div class="col-md-3 px-md-1">
        <base-input
            label="Username"
            placeholder="Username"
            :value="props.model.username"
        >
        </base-input>
      </div>
<!--      <div class="col-md-4 pl-md-1">-->
<!--        <base-input-->
<!--            label="Email address"-->
<!--            type="email"-->
<!--            placeholder="mike@email.com"-->
<!--        >-->
<!--        </base-input>-->
<!--      </div>-->
    </div>

  </card>
</template>


<script setup>
import BaseInput from "@/components/Inputs/BaseInput.vue";
import Card from "@/components/Cards/Card.vue";

// defineProps를 사용하여 model prop을 정의합니다.
const props = defineProps({
  model: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

defineExpose({
  props
});

</script>
<style></style>