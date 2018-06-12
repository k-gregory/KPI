import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-file-input',
  templateUrl: './file-input.component.html',
  styleUrls: ['./file-input.component.css']
})
export class FileInputComponent {
  file: any;

  @Input() caption: string;
  @Output() loaded = new EventEmitter<String>();

  constructor() { }

  fileChanged(ev){
    this.file = ev.target.files[0];

    const fr = new FileReader();
    fr.onload = (e)=>{
      this.loaded.emit(fr.result);
    };
    fr.readAsText(this.file);
  }
}
