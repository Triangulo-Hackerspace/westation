import { BaseEntity } from './../../shared';

export class Station implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public port?: number,
        public sufix?: string,
        public longitude?: number,
        public latitude?: number,
        public ip?: string,
        public sensors?: BaseEntity[],
        public checked: boolean = true,
    ) {
    }
}
