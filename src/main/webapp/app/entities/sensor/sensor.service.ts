import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Sensor } from './sensor.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SensorService {

    private resourceUrl =  SERVER_API_URL + 'api/sensors';

    constructor(private http: Http) { }

    create(sensor: Sensor): Observable<Sensor> {
        const copy = this.convert(sensor);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(sensor: Sensor): Observable<Sensor> {
        const copy = this.convert(sensor);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Sensor> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }
    findAll(): Observable<ResponseWrapper> {
        return this.http.get(this.resourceUrl)
            .map((res: Response) => this.convertResponse(res));
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Sensor.
     */
    private convertItemFromServer(json: any): Sensor {
        const entity: Sensor = Object.assign(new Sensor(), json);
        return entity;
    }

    /**
     * Convert a Sensor to a JSON which can be sent to the server.
     */
    private convert(sensor: Sensor): Sensor {
        const copy: Sensor = Object.assign({}, sensor);
        return copy;
    }
}
