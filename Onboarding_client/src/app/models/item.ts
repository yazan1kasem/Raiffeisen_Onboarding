export class Item {
  id: string;
  geraet: string;
  administration: string;
  software: string;
  suchbegriff: string;

  constructor(
    id: string,
    geraet: string,
    administration: string,
    software: string,
    suchbegriff: string
  ) {
    this.id = id;
    this.geraet = geraet;
    this.administration = administration;
    this.software = software;
    this.suchbegriff = suchbegriff;
  }
}
