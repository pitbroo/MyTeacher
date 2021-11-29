import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITimeShit } from '../time-shit.model';
import { TimeShitService } from '../service/time-shit.service';
import { TimeShitDeleteDialogComponent } from '../delete/time-shit-delete-dialog.component';

@Component({
  selector: 'jhi-time-shit',
  templateUrl: './time-shit.component.html',
})
export class TimeShitComponent implements OnInit {
  timeShits?: ITimeShit[];
  isLoading = false;

  constructor(protected timeShitService: TimeShitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.timeShitService.query().subscribe(
      (res: HttpResponse<ITimeShit[]>) => {
        this.isLoading = false;
        this.timeShits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITimeShit): number {
    return item.id!;
  }

  delete(timeShit: ITimeShit): void {
    const modalRef = this.modalService.open(TimeShitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.timeShit = timeShit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
