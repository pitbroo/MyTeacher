import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRanking } from '../ranking.model';
import { RankingService } from '../service/ranking.service';

@Component({
  templateUrl: './ranking-delete-dialog.component.html',
})
export class RankingDeleteDialogComponent {
  ranking?: IRanking;

  constructor(protected rankingService: RankingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rankingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
