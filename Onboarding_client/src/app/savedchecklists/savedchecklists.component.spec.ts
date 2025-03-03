import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedChecklistsComponent } from './savedchecklists.component';

describe('SavedchecklistsComponent', () => {
  let component: SavedChecklistsComponent;
  let fixture: ComponentFixture<SavedChecklistsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SavedChecklistsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SavedChecklistsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
