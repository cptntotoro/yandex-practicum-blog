package ru.practicum.dto.tag;

/**
 * DTO тега на добавление
 */
public class TagAddDto extends TagBaseDto {

    private TagAddDto(String title) {
        super(title);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends TagBaseDto.Builder<Builder> {
        public Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        public TagAddDto build() {
            return new TagAddDto(title);
        }
    }
}