package qa.automation.mappers;

public interface Mapper<TIn, TOut> {
  TOut map(TIn in);
}
