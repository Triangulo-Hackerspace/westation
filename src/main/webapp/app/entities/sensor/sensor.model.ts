import { BaseEntity } from './../../shared';

export class Sensor implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public topic?: string,
        public threshold?: number,
        public checked: boolean = true,
    ) {
    }
}
