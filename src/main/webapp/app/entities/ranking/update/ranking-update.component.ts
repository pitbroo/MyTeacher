import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRanking, Ranking } from '../ranking.model';
import { RankingService } from '../service/ranking.service';

@Component({
  selector: 'jhi-ranking-update',
  templateUrl: './ranking-update.component.html',
})
export class RankingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    points: [],
    nowa: [],
  });

  constructor(protected rankingService: RankingService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ranking }) => {
      this.updateForm(ranking);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ranking = this.createFromForm();
    if (ranking.id !== undefined) {
      this.subscribeToSaveResponse(this.rankingService.update(ranking));
    } else {
      this.subscribeToSaveResponse(this.rankingService.create(ranking));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRanking>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ranking: IRanking): void {
    this.editForm.patchValue({
      id: ranking.id,
      points: ranking.points,
      nowa: ranking.nowa,
    });
  }

  protected createFromForm(): IRanking {
    return {
      ...new Ranking(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      nowa: this.editForm.get(['nowa'])!.value,
    };
  }
}
