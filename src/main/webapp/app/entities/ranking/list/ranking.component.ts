import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRanking } from '../ranking.model';
import { RankingService } from '../service/ranking.service';
import { RankingDeleteDialogComponent } from '../delete/ranking-delete-dialog.component';

@Component({
  selector: 'jhi-ranking',
  templateUrl: './ranking.component.html',
})
export class RankingComponent implements OnInit {
  rankings?: IRanking[];
  isLoading = false;

  constructor(protected rankingService: RankingService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.rankingService.query().subscribe(
      (res: HttpResponse<IRanking[]>) => {
        this.isLoading = false;
        this.rankings = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRanking): number {
    return item.id!;
  }

  delete(ranking: IRanking): void {
    const modalRef = this.modalService.open(RankingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ranking = ranking;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
