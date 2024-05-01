import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

const incidentData = [
  { id: "e0918909-d1fe-4897-afe6-26e3f683777a", status: false, location: 'Wien', class: 'Atemnot', time: '12:30' },
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
  { id: 'FLO-1', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-2', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-3', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-4', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-5', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-6', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-7', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-8', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-9', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-10', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-11', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-12', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-13', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-14', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'FLO-15', type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 'FLO-16', type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 'FLO-17', type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 'FLO-18', type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 'FLO-19', type: 'Rettungswagen', location: 'Wien', assigned: true },
  { id: 'FLO-20', type: 'Rettungswagen', location: 'Wien', assigned: true },
];

const resourceDataAdditional = [
  { id: 'RKK-1', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-2', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-3', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-4', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-5', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-6', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-7', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-8', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-9', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-10', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-11', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-12', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-13', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-14', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-15', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-16', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-17', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-18', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-19', type: 'Rettungswagen', location: 'Wien', assigned: false },
  { id: 'RKK-20', type: 'Rettungswagen', location: 'Wien', assigned: false },
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
