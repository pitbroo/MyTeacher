import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPoint } from '../point.model';
import { PointService } from '../service/point.service';
import { PointDeleteDialogComponent } from '../delete/point-delete-dialog.component';

@Component({
  selector: 'jhi-point',
  templateUrl: './point.component.html',
})
export class PointComponent implements OnInit {
  points?: IPoint[];
  isLoading = false;

  constructor(protected pointService: PointService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pointService.query().subscribe(
      (res: HttpResponse<IPoint[]>) => {
        this.isLoading = false;
        this.points = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPoint): number {
    return item.id!;
  }

  delete(point: IPoint): void {
    const modalRef = this.modalService.open(PointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.point = point;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
