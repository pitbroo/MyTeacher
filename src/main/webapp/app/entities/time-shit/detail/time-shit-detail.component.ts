import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITimeShit } from '../time-shit.model';

@Component({
  selector: 'jhi-time-shit-detail',
  templateUrl: './time-shit-detail.component.html',
})
export class TimeShitDetailComponent implements OnInit {
  timeShit: ITimeShit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ timeShit }) => {
      this.timeShit = timeShit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
