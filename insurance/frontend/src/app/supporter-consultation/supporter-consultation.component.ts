import { Component, OnInit } from '@angular/core';
import {ClientInfo, SupporterConsultationService} from '../supporter-consultation.service';
import {isUndefined} from 'util';

@Component({
  selector: 'app-supporter-consultation',
  templateUrl: './supporter-consultation.component.html',
  styleUrls: ['./supporter-consultation.component.css']
})
export class SupporterConsultationComponent implements OnInit {
  constructor(public consultation: SupporterConsultationService) { }
  message: string = "";

  get clients(): ClientInfo[] {
    const res = [];
    for(let key in this.consultation.clients)
      res.push(this.consultation.clients[key])
    return res;
  }

  get currentMessages(){
    if(isUndefined(this.consultation.selectedClient)) return [];
    return this.consultation.clients[this.consultation.selectedClient].messages
  }

  selectClient(clientId: string){
    this.consultation.selectClient(clientId);
    this.consultation.clients[clientId].hasNewMessages = false;
  }

  assignClient(id: string){
    this.consultation.assignClient(id)
  }

  unassignClient(id: string){
    this.consultation.unassignClient(id)
  }

  answer(){
    this.consultation.writeAnswer(this.message);
  }

  ngOnInit() {
    this.consultation.refreshFree();
  }

}
