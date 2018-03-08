import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Station } from './station.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StationService {

    private resourceUrl =  SERVER_API_URL + 'api/stations';

    constructor(private http: Http) { }

    create(station: Station): Observable<Station> {
        const copy = this.convert(station);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(station: Station): Observable<Station> {
        const copy = this.convert(station);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Station> {
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
     * Convert a returned JSON object to Station.
     */
    private convertItemFromServer(json: any): Station {
        const entity: Station = Object.assign(new Station(), json);
        return entity;
    }

    /**
     * Convert a Station to a JSON which can be sent to the server.
     */
    private convert(station: Station): Station {
        const copy: Station = Object.assign({}, station);
        return copy;
    }
}
