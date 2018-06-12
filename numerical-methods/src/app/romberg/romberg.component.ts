import { Component, OnInit } from '@angular/core';
import {romberg, RombergResult} from "../romberg";
import {f, h, l} from "../f";

@Component({
  selector: 'app-romberg',
  templateUrl: './romberg.component.html',
  styleUrls: ['./romberg.component.css']
})
export class RombergComponent implements OnInit {
  e: string = "1e-9";
  result?: RombergResult = null;
  error?: string;

  constructor() {}

  ngOnInit() {
  }

  calc(){
    const e = parseFloat(this.e);
    if(isNaN(e)){
      this.error = "Can't parse accuracy";
      return;
    }

    const res = romberg(f, l, h, e);
    this.result = res;

    const maxLen = Math.max.apply(null, res.table.map(e=>e.length));
    for(const a of res.table){
      for(let i = a.length; i < maxLen; i++)
        a.push(null)
    }
  }

}
