import { Component, OnInit } from '@angular/core';
import {ClientConsultationService} from '../client-consultation.service';
import {SupportMessage} from '../../consultation/ConsultationMessage';

@Component({
  selector: 'app-client-consultation',
  templateUrl: './client-consultation.component.html',
  styleUrls: ['./client-consultation.component.css']
})
export class ClientConsultationComponent implements OnInit {
  currentMessage: string = "";
  messages: SupportMessage[];

  constructor(private consultation: ClientConsultationService) { }

  ngOnInit() {
    this.messages = this.consultation.messages;
  }

  addMessage(){
    this.consultation.writeQuestion(this.currentMessage);
    this.currentMessage="";
  }
}
