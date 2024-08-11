import { createApp } from 'vue'
import { mount, shallowMount } from '@vue/test-utils'
import { createTestingPinia } from '@pinia/testing'
import { describe, expect, it,vi } from 'vitest'

import AppVue from '@/App.vue'
import {createMemoryHistory, createRouter, createWebHistory, RouterView} from "vue-router";
import {useConnectStore} from '@/stores/connectStore.js';
import LoginView from "@/view/Login.vue";
import AboutView from "@/components/test/AboutView.vue";

// 라우터 정의
const router = createRouter({
    history: createMemoryHistory(),
    routes: [
        {
            path: '/',
            components: {
                login: LoginView,
            },
        },
        {
            path: '/about',
            components: {
                main: AboutView,
            },
        },

    ], // 여기에 필요한 라우트를 추가하세요
})


import HelloWorld  from "@/components/HelloWorld.vue";
describe('AppVue.vue', () => {
    it('mounted', () => {

        const pinia = createTestingPinia({createSpy: vi.fn})
        const app = createApp(AppVue)
        app.use(pinia)
        app.use(router)

        const store1 = useConnectStore();
        store1.setUuid("KK")

        console.log(` isLogin : ${store1.isLogin}`)
        const wrapper = mount(AppVue
        //     ,{
        //     props: {
        //         store: store1
        //     },
        //     global:{
        //         plugins:[app]
        //     }
        // }
        )

        const msgProp = wrapper.props();
        expect(msgProp).toBe('KKKK');
        // const routerView = wrapper.props().msg
        // expect(routerView.exists()).toBe(true)
        // expect(routerView.props('name')).toBe('main')
    })

    // it('width props에 따라 palette 컨테이너의 width가 결정된다.', () => {
    //     const width = '200'
    //     const wrapper = mount(AppVue, {
    //         props: {
    //             modelValue: '',
    //             palette: [],
    //             width,
    //         },
    //     })
    //     const style = wrapper.find('.palette').attributes('style')
    //     expect(style?.includes('width')).toBe(true)
    //     expect(style).toContain(`width: ${width}px`)
    // })
    // const palettes = ['#F5EFE7', '#D8C4B6', '#4F709C']
    //
    // it('palettes props에 따라 palette item이 설정된다.', () => {
    //     const wrapper = mount(AppVue, {
    //         props: {
    //             palette: palettes,
    //             modelValue: '',
    //         },
    //     })
    //     expect(wrapper.findAll('.palette-item')).toHaveLength(palettes.length)
    //
    // })
    //
    // it('팔레트 선택시 update:modelValue emit이벤트가 발생된다.', async () => {
    //     const wrapper = mount(AppVue, {
    //         props: {
    //             palette: palettes,
    //             modelValue: '',
    //         },
    //     })
    //     await wrapper.find('.palette-item').trigger('click')
    //     const emitEvt = wrapper.emitted('update:modelValue')
    //     expect(emitEvt).toHaveLength(1)
    //     expect(emitEvt[0][0]).toEqual(palettes[0])
    // })
    //
    // it('팔레트 modelValue가 업데이트 되었을 시 선택되었음을 의미하는 UI가 활성화 된다.', async () => {
    //     const wrapper = shallowMount(AppVue, {
    //         props: {
    //             palette: palettes,
    //             modelValue: '',
    //         },
    //     })
    //     expect(wrapper.find('.palette-item__picked').exists()).toBe(false)
    //     await wrapper.setProps({ modelValue: palettes[0] })
    //     expect(wrapper.find('.palette-item__picked').exists()).toBe(true)
    // })
})
