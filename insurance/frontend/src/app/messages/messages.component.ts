import {Component, Input, OnInit} from '@angular/core';
import {SupportMessage} from '../../consultation/ConsultationMessage';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  @Input("list") messages: SupportMessage[];

  constructor() { }

  ngOnInit() {
  }

}
