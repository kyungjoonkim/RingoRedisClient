// sum.test.js
import { expect, test } from 'vitest'
import { sum } from '@/sum.js'

test('adds 1 + 2 to equal 3', () => {
    console.log("Test")
    expect(sum(1, 2)).toBe(3)
})