import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {MediaMatcher} from "@angular/cdk/layout";

import {routes} from "./routes";
import {ActivatedRoute, Router, RoutesRecognized} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnDestroy, OnInit {
  private currentMethodName? : string;
  opened = true;
  routes : {name: string, path: string}[];

  mobileQuery: MediaQueryList;

  private _mobileQueryListener: () => void;

  constructor(changeDetectorRef: ChangeDetectorRef, media: MediaMatcher, private router: Router) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);


    this.routes = routes.map(r=>({
      name: <string>r.data.name,
      path: r.path
    }))
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

  get title(): string {
    if(this.currentMethodName){
      return "Чисельні методи: " + this.currentMethodName;
    } else {
      return "Чисельні методи";
    }
  }

  ngOnInit(): void {
    this.router.events.subscribe((d)=>{
      if(d instanceof RoutesRecognized){
        this.currentMethodName = d.state.root.firstChild.data.name;
      }
    })
  }
}
