import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

const incidentData = [
  { id: 1, status: false, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 2, status: false, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 3, status: false, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 4, status: false, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 5, status: false, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 6, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 7, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 8, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 9, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 10, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 11, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 12, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 13, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 14, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 15, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },
  { id: 16, status: true, location: 'Wien', class: 'Atemnot', time: '12:30' },

];

const resourceData = [
  { id: 1, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 2, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 3, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 4, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 5, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 6, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 7, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 8, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 9, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 10, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 11, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 12, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 13, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 14, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 15, type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 16, type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 17, type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 18, type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 19, type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 20, type: 'Rettungswagen', location: 'Wien', assigned: true },
];

const resourceDataAdditional = [
  { id: 1, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 2, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 3, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 4, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 5, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 6, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 7, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 8, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 9, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 10, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 11, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 12, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 13, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 14, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 15, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 16, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 17, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 18, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 19, type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 20, type: 'Rettungswagen', location: 'Wien', assigned: false },
];

export const mockInterceptor: HttpInterceptorFn = (req, next) => {
  if(req.method === "GET" && req.url === "http://localhost:4200/incidents") {
    return of(new HttpResponse({ status: 200, body: incidentData }));
} else if(req.method === "GET" && req.url.startsWith("http://localhost:4200/incidents/")) {
   return of(new HttpResponse({ status: 200, body: incidentData[0]}));
}
else if(req.method === "GET" && req.url === "http://localhost:4200/resources?additional=false") {
  return of(new HttpResponse({ status: 200, body: resourceData }));
}
else if(req.method === "GET" && req.url === "http://localhost:4200/resources?additional=true") {
  return of(new HttpResponse({ status: 200, body: resourceDataAdditional }));
}

  return next(req);
};
